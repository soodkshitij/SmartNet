import boto3
from datetime import datetime
import uuid
# session = boto3.Session(
#     aws_access_key_id="AKIAJHS6H6Z7UCCH3OUQ",
#     aws_secret_access_key="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",
# )
#s3 = session.resource('s3')
s3 = boto3.resource('s3')
bucketname ="" 
for bucket in s3.buckets.all():
    bucketname= bucket.name




def upload(path):
    data = open(path, 'rb')
    key = str(uuid.uuid4().hex)
    s3.Bucket(bucketname).put_object(Key=key, Body=data)
    return key