#include <stdio.h>
#include <stdlib.h>
#include <stdarg.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/time.h>
#include <machbase_sqlcli.h>

#define RC_SUCCESS              0
#define RC_FAILURE              -1

#define DESC_LEN   20
#define ARRAY_SIZE 30

#define UNUSED(aVar) do { (void)(aVar); } while(0)

SQLHENV     gEnv;
SQLHDBC     gCon;

static char          gTargetIP[16] = "127.0.0.1";
static int           gPortNo=5656;

void printError(SQLHENV aEnv, SQLHDBC aCon, SQLHSTMT aStmt, char *aMsg);
int connectDB();
int executeDirectSQL(const char *aSQL, int aErrIgnore);
int createTable();
int prepareArrayInsertTable();
int selectAllRecords();
void disconnectDB();

time_t getTimeStamp()
{
    struct timeval sTimeVal;
    int            sRet;

    sRet = gettimeofday(&sTimeVal, NULL);

    if (sRet == 0)
    {
        return (time_t)(sTimeVal.tv_sec * 1000000 + sTimeVal.tv_usec);
    }
    else
    {
        return 0;
    }
}

void printError(SQLHENV aEnv, SQLHDBC aCon, SQLHSTMT aStmt, char *aMsg)
{
    SQLINTEGER      sNativeError;
    SQLCHAR         sErrorMsg[SQL_MAX_MESSAGE_LENGTH + 1];
    SQLCHAR         sSqlState[SQL_SQLSTATE_SIZE + 1];
    SQLSMALLINT     sMsgLength;

    if( aMsg != NULL )
    {
        printf("%s\n", aMsg);
    }

    if( SQLError(aEnv, aCon, aStmt, sSqlState, &sNativeError,
        sErrorMsg, SQL_MAX_MESSAGE_LENGTH, &sMsgLength) == SQL_SUCCESS )
    {
        printf("SQLSTATE-[%s], Machbase-[%d][%s]\n", sSqlState, sNativeError, sErrorMsg);
    }
}

int connectDB()
{
    char sConnStr[1024];

    if( SQLAllocEnv(&gEnv) != SQL_SUCCESS ) 
    {
        printf("SQLAllocEnv error\n");
        goto error;
    }

    if( SQLAllocConnect(gEnv, &gCon) != SQL_SUCCESS ) 
    {
        printf("SQLAllocConnect error\n");

        SQLFreeEnv(gEnv);
        gEnv = SQL_NULL_HENV;

        goto error;
    }

    sprintf(sConnStr,"SERVER=%s;UID=SYS;PWD=MANAGER;CONNTYPE=1;PORT_NO=%d", gTargetIP, gPortNo);

    if( SQLDriverConnect( gCon, NULL,
                          (SQLCHAR *)sConnStr,
                          SQL_NTS,
                          NULL, 0, NULL,
                          SQL_DRIVER_NOPROMPT ) != SQL_SUCCESS
      )
    {
        printError(gEnv, gCon, NULL, "SQLDriverConnect error");

        SQLFreeConnect(gCon);
        gCon = SQL_NULL_HDBC;

        SQLFreeEnv(gEnv);
        gEnv = SQL_NULL_HENV;

        goto error;
    }

    return RC_SUCCESS;
    
error:
    return RC_FAILURE;
}

void disconnectDB()
{
    if( SQLDisconnect(gCon) != SQL_SUCCESS )
    {
        printError(gEnv, gCon, NULL, "SQLDisconnect error");
    }

    SQLFreeConnect(gCon);
    gCon = SQL_NULL_HDBC;

    SQLFreeEnv(gEnv);
    gEnv = SQL_NULL_HENV;
}

int executeDirectSQL(const char *aSQL, int aErrIgnore)
{
    SQLHSTMT sStmt = SQL_NULL_HSTMT;

    if( SQLAllocStmt(gCon, &sStmt) != SQL_SUCCESS )
    {
        if( aErrIgnore == 0 )
        {
            printError(gEnv, gCon, sStmt, "SQLAllocStmt Error");
            goto error;
        }
    }
    
    if( SQLExecDirect(sStmt, (SQLCHAR *)aSQL, SQL_NTS) != SQL_SUCCESS )
    {
        if( aErrIgnore == 0 )
        {
            printError(gEnv, gCon, sStmt, "SQLExecDirect Error");

            SQLFreeStmt(sStmt,SQL_DROP);
            sStmt = SQL_NULL_HSTMT;
            goto error;
        }
    }

    if( SQLFreeStmt(sStmt, SQL_DROP) != SQL_SUCCESS )
    {
        if (aErrIgnore == 0)
        {
            printError(gEnv, gCon, sStmt, "SQLFreeStmt Error");
            sStmt = SQL_NULL_HSTMT;
            goto error;
        }
    }

    sStmt = SQL_NULL_HSTMT;
    return RC_SUCCESS;

error:
    return RC_FAILURE;
}

int createTable()
{
    int sRC;

    sRC = executeDirectSQL("DROP TABLE BIND_ARRAY", 1);
    if( sRC != RC_SUCCESS )
    {
        sRC = 0;
    }

    sRC = executeDirectSQL("CREATE TABLE BIND_ARRAY(id uinteger, id2 varchar(15), id3 double)", 0);
    if( sRC != RC_SUCCESS )
    {
        return RC_FAILURE;
    }

    return RC_SUCCESS;
}

int prepareArrayInsertTable()
{

    SQLHSTMT       sStmt;
    unsigned long  i;
    /* DATA */
    SQLCHAR        sDescArray[ARRAY_SIZE][DESC_LEN] = {{0,}, };
    SQLUINTEGER    sPartIDArray[ARRAY_SIZE];
    SQLDOUBLE      sPriceArray[ARRAY_SIZE];
    /* Indicator */
    SQLLEN         sDescLenOrIndArray[ARRAY_SIZE];
    SQLLEN         sPartIDIndArray[ARRAY_SIZE];
    SQLLEN         sPriceIndArray[ARRAY_SIZE];
    /* Results */
    SQLUSMALLINT   sParamStatusArray[ARRAY_SIZE];
    SQLULEN        sParamsProcessed;

    const char     *sSQL = "INSERT INTO BIND_ARRAY VALUES(?, ?, ?)";

    memset(sDescLenOrIndArray, 0, sizeof(sDescLenOrIndArray));
    memset(sPartIDIndArray, 0, sizeof(sPartIDIndArray));
    memset(sPriceIndArray, 0, sizeof(sPriceIndArray));

    printf("prepare insert start\n");

    if (SQLAllocStmt(gCon, &sStmt) == SQL_ERROR) {
        printf("AllocStmt error\n");
        goto error;
    }

    if (SQLPrepare(sStmt, (SQLCHAR *)sSQL, SQL_NTS) == SQL_ERROR) {
        printf("Prepare error[%s] \n", sSQL);
        goto error;
    }

    // Set the SQL_ATTR_PARAM_BIND_TYPE statement attribute to use
    // column-wise binding.
    SQLSetStmtAttr(sStmt, SQL_ATTR_PARAM_BIND_TYPE, SQL_PARAM_BIND_BY_COLUMN, 0);

    // Specify the number of elements in each parameter array.
    SQLSetStmtAttr(sStmt, SQL_ATTR_PARAMSET_SIZE, (SQLPOINTER)ARRAY_SIZE, 0);

    // Specify an array in which to return the status of each set of
    // parameters.
    SQLSetStmtAttr(sStmt, SQL_ATTR_PARAM_STATUS_PTR, sParamStatusArray, 0);

    // Specify an SQLUINTEGER value in which to return the number of sets of
    // parameters processed.
    SQLSetStmtAttr(sStmt, SQL_ATTR_PARAMS_PROCESSED_PTR, &sParamsProcessed, 0);

    // Bind the parameters in column-wise fashion.
    SQLBindParameter(sStmt, 1, SQL_PARAM_INPUT, SQL_C_ULONG, SQL_INTEGER, 0, 0,
                     sPartIDArray, 0, (SQLLEN *)sPartIDIndArray);
    SQLBindParameter(sStmt, 2, SQL_PARAM_INPUT, SQL_C_CHAR, SQL_VARCHAR, DESC_LEN, 0,
                     sDescArray, DESC_LEN, (SQLLEN *)sDescLenOrIndArray);
    SQLBindParameter(sStmt, 3, SQL_PARAM_INPUT, SQL_C_DOUBLE, SQL_DOUBLE, 7, 0,
                     sPriceArray, 0, (SQLLEN *)sPriceIndArray);

    // Set Data & indicator
    for (i = 0; i < ARRAY_SIZE; i++) {
        /* data */
        sprintf((char *)(sDescArray[i]), "TEXT-%08ld", i);
        sPartIDArray[i] = 100 + i;
        sPriceArray[i] = 1.11 + i;
        /* indicator */
        sDescLenOrIndArray[i] = SQL_NTS;
        sPartIDIndArray[i] = 0;
        sPriceIndArray[i] = 0;
    }

    if (SQLExecute(sStmt) != RC_SUCCESS) {
        printf("Preapred-Execute error\n");
        goto error;
    }

    // Check to see which sets of parameters were processed successfully.
    for (i = 0; i < sParamsProcessed; i++) {
        printf("Parameter Set  Status\n");
        printf("-------------  -------------\n");
        switch (sParamStatusArray[i]) {
            case SQL_PARAM_SUCCESS:
            case SQL_PARAM_SUCCESS_WITH_INFO:
                printf("%13ld  Success\n", i);
                break;
            case SQL_PARAM_ERROR:
                printf("%13ld  Error\n", i);
                break;

            case SQL_PARAM_UNUSED:
                printf("%13ld  Not processed\n", i);
                break;

            case SQL_PARAM_DIAG_UNAVAILABLE:
                printf("%13ld  Unknown\n", i);
                break;
        }
    }
    printf("prepare insert end\n");

    if ( SQLFreeStmt(sStmt, SQL_DROP) != RC_SUCCESS )
    {
        printf("FreeStmt failure\n");
        goto error;
    }
    return RC_SUCCESS;

error:
    return RC_FAILURE;
}

int selectAllRecords()
{
    SQLHSTMT          sStmt;
    unsigned long     sData_1[3]={0,};
    char              sData_2[DESC_LEN]={0,};
    double            sData_3[3]={0,};
    unsigned int      i = 0;
    SQLLEN            sLen[3] = {0, };

    const char *sSQL = "select * from BIND_ARRAY order by 1";

    if( SQLAllocStmt(gCon, &sStmt) != RC_SUCCESS )
    {
        printf("AllocStmt error\n");
        goto error;
    }

    if( SQLPrepare(sStmt, (SQLCHAR *)sSQL, SQL_NTS) != RC_SUCCESS )
    {
        printf("Prepare error[%s]\n", sSQL);
        goto error;
    }

    if( SQLExecute(sStmt) != RC_SUCCESS )
    {
        printf("Prepared-Execute error\n");
        goto error;
    }

    if( SQLBindCol(sStmt, 1, SQL_C_ULONG, sData_1, sizeof(sData_1), &sLen[0]) != RC_SUCCESS )
    {
        printf("bindcol c_ulong error\n");
        goto error;
    }
    if( SQLBindCol(sStmt, 2, SQL_C_CHAR, sData_2, sizeof(sData_2), &sLen[1]) != RC_SUCCESS )
    {
        printf("bindcol c_char error\n");
        goto error;
    }
    if( SQLBindCol(sStmt, 3, SQL_C_DOUBLE, sData_3, sizeof(sData_3), &sLen[2]) != RC_SUCCESS )
    {
        printf("bindcol c_float error\n");
        goto error;
    }

    printf("SQLBindCol  & Begin Fetch------------------------------------\n");

    while ( SQLFetch(sStmt) == SQL_SUCCESS )
    {
        sData_2[13] = '\000';
        printf("[%02d]", i++);
        printf("    [%ld][%s][%0.2f]\n", sData_1[0], sData_2, sData_3[0]);
    }
    printf("End of Fetch----------\n");

    if ( SQLFreeStmt(sStmt, SQL_DROP) != RC_SUCCESS ) 
    {
        printf("FreeStmt error\n");
    }
    return RC_SUCCESS;

error:
    return RC_FAILURE;
}

int main()
{
    SQLHSTMT    sStmt = SQL_NULL_HSTMT;
    time_t      sStartTime;
    time_t      sEndTime;

    if( connectDB() == RC_SUCCESS )
    {
        printf("connectDB success\n");
    }
    else
    {
        printf("connectDB failure\n");
        goto error;
    }
    
    if( createTable() == RC_SUCCESS )
    {
        printf("createTable success\n");
    }
    else
    {
        printf("createTable failure\n");
        goto error;
    }

    sStartTime = getTimeStamp();
    if( prepareArrayInsertTable() == RC_SUCCESS )
    {
        sEndTime = getTimeStamp();
        printf("insert success\n");
        printf("timegap = %ld microseconds\n\n", sEndTime - sStartTime);
    }
    else
    {
        printf("insert failure\n");
        goto error;
    }

    if ( selectAllRecords() != RC_SUCCESS )
    {
        printf("selectTable failure\n");
        goto error;
    }

    disconnectDB();
    return RC_SUCCESS;

error:
    if( sStmt != SQL_NULL_HSTMT )
    {
        SQLFreeStmt(sStmt, SQL_DROP);
        sStmt = SQL_NULL_HSTMT;
    }

    if( gCon != SQL_NULL_HDBC )
    {
        disconnectDB();
    }

    return RC_FAILURE;
}