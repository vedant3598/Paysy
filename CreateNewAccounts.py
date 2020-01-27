import json
from algosdk import account, mnemonic

acct = account.generate_account()
address1 = acct[1]
print("Account 1")
print(address1)
mnemonic1 = mnemonic.from_private_key(acct[0])

print("Account 2")
acct = account.generate_account()
address2 = acct[1]
print(address2)
mnemonic2 = mnemonic.from_private_key(acct[0])

print("mnemonic1 = \"{}\"".format(mnemonic1))
print("mnemonic2 = \"{}\"".format(mnemonic2))

#Account 1
#5SPFCYJSRSMMWKNPAOAK6QPO3PGRHMMZGURVJ4HGI4XSAMYFRWTJXAMXDY
#Account 2
#MSVOSPBXU5NFII4AHOM3CH4NLF6GFAXNGC4DWSC6E33VWOI4DNZ7DC2JAQ
#mnemonic1 = "like allow recall gesture subject ready pony bracket gas wealth nephew unfold hedgehog donor lion husband frequent boring cloth thrive razor domain term absorb similar"
#mnemonic2 = "mind cradle veteran today cross marriage collect vicious sun castle fringe brain reform glad delay palm else joy eyebrow blanket speed bring point absorb trumpet"



#ZKWBHOJRL6SHCK6L3QM46TD5FT2TBU2SH5L7AKG3X5H23IMU4BLYLHNSPE
#oblige pet boost dawn devote depend leopard rate potato soda wet hobby puzzle rose flip wall slight gorilla alcohol seven rigid phrase spoil absorb else


#WXYWCBF76MEKX3DQ6LAAQBSHVCJHV3NTPHJWCRWOP7X6VUXIQQPFI2J5GU
#spirit spider walk call elbow struggle promote birth borrow section credit people fashion obtain sentence shadow inmate very enhance what thank pilot hurry abstract hundred