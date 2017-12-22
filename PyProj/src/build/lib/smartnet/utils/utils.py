import datetime
import time
import jwt


def to_py_date(java_timestamp):
    try:
        seconds = java_timestamp / 1000
        sub_seconds  = (java_timestamp % 1000.0) / 1000.0
        date = datetime.datetime.fromtimestamp(seconds + sub_seconds)
    except:
        #some error here. return None
        return None

    return date

def to_java_date(py_timestamp):
    try:
        java_date =  int(time.mktime(py_timestamp.timetuple())) * 1000 + py_timestamp.microsecond / 1000
        return java_date
    except:
        #some error here, return None
        return None


def encode_auth_token(user_id):
    """
    Generates the Auth Token
    :return: string
    """
    try:
        payload = {
            'exp': datetime.datetime.utcnow() + datetime.timedelta(days=7, seconds=0),
            'iat': datetime.datetime.utcnow(),
            'sub': user_id
        }
        return jwt.encode(
            payload,
            'w\xd6\xeb\x0b\xef\xe5\x15\xb1\x0b\xbb\x16&\x98fX\xcf\x80>\xa3dn)\x8e\x88',
            algorithm='HS256'
        )
    except Exception as e:
        return None

def decode_auth_token(auth_token):
    """
    Decodes the auth token
    :param auth_token:
    :return: integer|string
    """
    try:
        payload = jwt.decode(auth_token,'w\xd6\xeb\x0b\xef\xe5\x15\xb1\x0b\xbb\x16&\x98fX\xcf\x80>\xa3dn)\x8e\x88' )
        return payload['sub']
    except jwt.ExpiredSignatureError:
        raise
    except jwt.InvalidTokenError:
        raise
