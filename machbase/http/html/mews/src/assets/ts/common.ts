const gLENGTH = 23;

function random() {
    return Math.ceil(Math.random() * 1024);
}

function comma(aNum) {
    if (typeof aNum !== 'number') return aNum;

    const sNum = aNum.toString();
    const sPointIdx = sNum.indexOf('.');

    let sFixed = '';
    let sStr = '';

    if (sPointIdx >= 0) { // having point
        sFixed = sNum.slice(sPointIdx);
    }

    if (sNum.length < 4) {
        return sFixed === '' ? sNum : sNum + sFixed;
    }

    sNum.split('').reverse().forEach((val, idx) => {
        if ((idx + 1) % 3 === 0 && (idx !== sNum.length - 1)) {
            sStr = `,${val}${sStr}`;
        } else {
            sStr = `${val}${sStr}`;
        }
    });

    return sFixed === '' ? sStr : sStr + sFixed;
}

function fit(aStr: string) {
    const sLength = aStr.length;

    let sStr = '';
    
    if (sLength > gLENGTH) return aStr;

    for (let i = 0; i < gLENGTH - sLength; i++) {
        sStr += ' ';
    }

    sStr += aStr;

    return sStr;
}

function gigabyte(aByte: number) {
    if (typeof aByte !== 'number') return aByte;

    return (aByte / 1024 / 1024 / 1024).toFixed(1);
}

export {
    comma,
    fit,
    gigabyte,
    random,
}