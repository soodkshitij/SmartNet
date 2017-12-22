import datetime

from elixir import metadata, setup_all
import elixir
from elixir.entity import Entity
from elixir.fields import Field
from elixir.options import using_options, using_table_options
from elixir.relationships import OneToMany, ManyToOne
from sqlalchemy import create_engine
from sqlalchemy.types import Integer, String, DateTime, Float, Boolean, Text, Enum, BigInteger, Date
from MySQLdb.constants.FLAG import AUTO_INCREMENT


class Community(Entity):
    id = Field(Integer,primary_key=True)
    name = Field(String(255))
    street_address = Field(String(255))
    city = Field(String(20))
    state = Field(String(20))
    zipcode = Field(String(6))
    country = Field(String(20))
    created_by = Field(Integer)
    created_timestamp = Field(DateTime)
    geo_id = Field(Integer)
    type = Field(Integer)
    status = Field(Integer)
    using_options(tablename="Community")
    using_table_options(mysql_engine="InnoDB")
    
class GeographicalData(Entity):
    id = Field(Integer,primary_key=True)
    google_place_id = Field(String(255))
    lat = Field(String(20))
    lon = Field(String(20))
    using_options(tablename="GeoData")
    using_table_options(mysql_engine="InnoDB")
    
class Admin(Entity):
    id = Field(Integer,primary_key=True)
    first_name = Field(String(20))
    last_name = Field(String(20))
    password = Field(String(255))
    email = Field(String(50))
    phone_number = Field(String(20))
    role = Field(Integer)
    community_id = Field(Integer)
    using_options(tablename="admin")
    using_table_options(mysql_engine="InnoDB")
    
class User(Entity):
    id = Field(Integer,primary_key=True)
    first_name = Field(String(255))
    last_name = Field(String(255))
    password = Field(String(255))
    email = Field(String(255))
    phone_number = Field(String(10))
    community_id = Field(Integer)
    verified =  Field(Boolean, default='0',server_default='0')
    using_options(tablename="User")
    using_table_options(mysql_engine="InnoDB")

class Comment(Entity):
    id = Field(Integer,primary_key=True)
    content = Field(Text)
    user_id = Field(Integer)
    post_id = Field(Integer)
    using_options(tablename="Comment")
    using_table_options(mysql_engine="InnoDB")

class InterPost(Entity):
    id = Field(Integer,primary_key=True)
    title = Field(String(255))
    content = Field(Text)
    timestamp = Field(DateTime)
    user_id = Field(Integer)
    community_id = Field(Integer)
    category = Field(String(20))
    admin_approved = Field(Boolean,default='0',server_default='0')
    file_id =  Field(Integer)
    using_options(tablename="InterPost")
    using_table_options(mysql_engine="InnoDB")

class IntraPost(Entity):
    id = Field(Integer,primary_key=True)
    title = Field(String(255))
    content = Field(Text)
    category = Field(String(20))
    timestamp = Field(DateTime)
    user_id = Field(Integer)
    community_id = Field(Integer)
    post_type = Field(Integer)
    file_id = Field(Integer)
    using_options(tablename="IntraPost")
    using_table_options(mysql_engine="InnoDB")

class Message(Entity):
    id = Field(Integer,primary_key=True)
    title = Field(String(255))
    content = Field(Text)
    timestamp = Field(DateTime)
    from_user_id = Field(Integer)
    to_user_id = Field(Integer)
    using_options(tablename="Message")
    using_table_options(mysql_engine="InnoDB")        
    
class ForgotPassword(Entity):
    id = Field(Integer,primary_key=True)
    uuid = Field(String(255))
    timestamp = Field(DateTime)
    user_id = Field(Integer)
    used = Field(Boolean)
    using_options(tablename="ForgotPassword")
    using_table_options(mysql_engine="InnoDB")        
    
class FileObject(Entity):
    id = Field(Integer,primary_key=True)
    key = Field(String(255))
    using_options(tablename="FileObject")
    using_table_options(mysql_engine="InnoDB")   


def initialize(dbname='SmartCommunity', db_hostname="localhost", setup=True):
    cengine = create_engine('mysql://root:pioneer@34.214.249.107'+'/' + dbname, pool_recycle=7200)
    metadata.bind = cengine
    metadata.bind.echo = True
    print "34.214.249.107"
    setup_all(setup)

if __name__=="__main__":

    initialize()