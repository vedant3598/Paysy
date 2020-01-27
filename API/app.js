const express = require('express');
const app = express();
const bodyParser = require('body-parser');
const algosdk = require('algosdk');

const token = "ef920e2e7e002953f4b29a8af720efe8e4ecc75ff102b165e0472834b25832c1";
const server = "http://hackathon.algodev.network"
const port = "9100"

let algodclient = new algosdk.Algod(token, server, port);

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.use( (req, res, next) => {
    if(req.method === "OPTIONS") res.header('Access-Control-Allow-Origin', req.headers.origin ? req.headers.origin : "*");
    else res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Authorization, Accept");
    next();
});

app.get('/generateAccount',(req,res,next)=>{
    var account = algosdk.generateAccount();
    console.log(account.addr);
    var mnemonic = algosdk.secretKeyToMnemonic(account.sk);
    res.status(200).json({
        accountAddr: account.addr,
        mnemonic: mnemonic
    });
});

app.get('/latestID/:cost',(req, res, next) => {
    console.log('signing transaction'); 
(async() => {
    //Get the relevant params from the algod
    let params = await algodclient.getTransactionParams();
    let endRound = params.lastRound + parseInt(10000);
    const mnemonic = "oblige pet boost dawn devote depend leopard rate potato soda wet hobby puzzle rose flip wall slight gorilla alcohol seven rigid phrase spoil absorb else";
    const recoveredAccount = algosdk.mnemonicToSecretKey(mnemonic); 
    console.log(recoveredAccount.addr);
    
    //create a transaction
    //note that the closeRemainderTo parameter is commented out
    //This parameter will clear the remaining funds in an account and 
    //send to the specified account if main transaction commits
    console.log('txn created');
    console.log("params: " + JSON.stringify(params));
    let txn = {
        "from": recoveredAccount.addr,
        "to": "WXYWCBF76MEKX3DQ6LAAQBSHVCJHV3NTPHJWCRWOP7X6VUXIQQPFI2J5GU",
        "fee": 1000,
        "amount": 1,
        "firstRound": params.lastRound,
        "lastRound": endRound,
        "genesisID": params.genesisID,
        "genesisHash": params.genesishashb64,
        // "note": new Uint8Array(0),
        //"closeRemainderTo": "IDUTJEUIEVSMXTU4LGTJWZ2UE2E6TIODUKU6UW3FU3UKIQQ77RLUBBBFLA"
    };
    console.log('signing txn');
    console.log('txn: ' + JSON.stringify(txn));
    //sign the transaction
    let signedTxn = algosdk.signTransaction(txn, recoveredAccount.sk);
    //let signedTxn = account.signTransaction(txn);
    console.log('submitting');
    console.log('signed trxn: ' + JSON.stringify(signedTxn));
    //submit the transaction
    let tx = await algodclient.sendRawTransaction(signedTxn.blob);
    console.log("Transaction : " + tx.txId);
        res.status(200).json({
            tx_id: tx.txId
        });
    })().catch(e => {
        console.log(e.message);
        res.status(500).json({
            error: 'error occured.'
        });
    });
});

app.get('/',(req,res,next) => {
    res.status(200).json({
        message: 'It works!'
    });
});

module.exports = app;