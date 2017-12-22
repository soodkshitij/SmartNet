import hashlib

a = hashlib.md5('alsdkfjasldfjkasdlf')
b = a.hexdigest()
print b
print int(b,16)

a = hashlib.md5('alsdkfjasldfjkasdlf')
b = a.hexdigest()
print b
print int(b,16)