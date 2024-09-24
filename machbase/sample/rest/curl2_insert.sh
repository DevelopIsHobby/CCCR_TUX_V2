DATA='[1,"aaa"],[2,"bbb"]'
#for i in {3..10000}
#do
#    DATA="$DATA,[$i,\"ccc$i\"]"
#done

#echo $DATA

curl -X POST -H "Content-Type: application/json" "http://127.0.0.1:5657/machbase" -d '{"name":"curl_sample", "values":['$DATA']}'
echo ""
