from algosdk import algod, mnemonic, transaction, account
# from flask import Flask
# from flask_cors import CORS

# app = Flask(__name__)
# cors = CORS(app)

# @app.route('/')
# def signTransaction():
    

#     return (txid)

# if __name__ == "__main__":
#     app.run(host="localhost",port="8081")

# passphrase = "like allow recall gesture subject ready pony bracket gas wealth nephew unfold hedgehog donor lion husband frequent boring cloth thrive razor domain term absorb similar"

# acl = algod.AlgodClient("H9IknTatc8DgHA3kOa3m4YyCvQCOZV7xUtWvq640", "https://testnet-algorand.api.purestake.io/ps1")

# # convert passphrase to secret key
# sk = mnemonic.to_private_key(passphrase)

# # get suggested parameters
# params = acl.suggested_params()
# gen = params["genesisID"]
# gh = params["genesishashb64"]
# last_round = params["lastRound"]
# fee = params["fee"]

# # Set other parameters
# amount = 10
# note = "Some Text".encode()
# receiver = "MSVOSPBXU5NFII4AHOM3CH4NLF6GFAXNGC4DWSC6E33VWOI4DNZ7DC2JAQ"

# # create the transaction
# txn = transaction.PaymentTxn(account.address_from_private_key(sk), fee, last_round, last_round+1000, gh, receiver, amount, note=note)

# # sign it
# stx = txn.sign(sk)

# # send it
# txid = acl.send_transaction(stx)

from algosdk import algod, mnemonic, transaction, account

passphrase = "like allow recall gesture subject ready pony bracket gas wealth nephew unfold hedgehog donor lion husband frequent boring cloth thrive razor domain term absorb similar"

acl = algod.AlgodClient("ef920e2e7e002953f4b29a8af720efe8e4ecc75ff102b165e0472834b25832c1", "http://hackathon.algodev.network:9100/")

# convert passphrase to secret key
sk = mnemonic.to_private_key(passphrase)

# get suggested parameters
params = acl.suggested_params()
gen = params["genesisID"]
gh = params["genesishashb64"]
last_round = params["lastRound"]
fee = params["fee"]

# Set other parameters
amount = 10
note = "Special Dish".encode()
receiver = "receiver Algorand Address"

# create the transaction
txn = transaction.PaymentTxn(account.address_from_private_key(sk), fee, last_round, last_round+1000, gh, receiver, amount, note=note)

# sign it
stx = txn.sign(sk)

# send it
txid = acl.send_transaction(stx)