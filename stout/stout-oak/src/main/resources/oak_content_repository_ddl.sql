  -- Sets up tables for use by Oak Content Management Repository.  The file below is targeted for SQL Server.  You can see 
  -- Oak's documenatation on how to easily generate DDL for other databases at the following link:
  -- https://jackrabbit.apache.org/oak/docs/nodestore/document/rdb-document-store.html
  
  -- Currently, Oak does not respect schemas and just uses the default schema for your database.  An improvement has been 
  -- submitted to the Oak team to resolve this inconvenience: https://issues.apache.org/jira/browse/OAK-8470
  
  -- creating table CLUSTERNODES for schema version 2
  create table CLUSTERNODES (ID varbinary(512) not null, MODIFIED bigint, HASBINARY smallint, DELETEDONCE smallint, 
  			   MODCOUNT bigint, CMODCOUNT bigint, DSIZE bigint, VERSION smallint, SDTYPE smallint, SDMAXREVTIME bigint, 
  			   DATA nvarchar(4000), BDATA varbinary(max), constraint CLUSTERNODES_PK primary key clustered (ID ASC))
  create index CLUSTERNODES_MOD on CLUSTERNODES (MODIFIED)
  create index CLUSTERNODES_VSN on CLUSTERNODES (VERSION)
  create index CLUSTERNODES_SDT on CLUSTERNODES (SDTYPE) where SDTYPE is not null
  create index CLUSTERNODES_SDM on CLUSTERNODES (SDMAXREVTIME) where SDMAXREVTIME is not null

  -- creating table JOURNAL for schema version 2
  create table JOURNAL (ID varbinary(512) not null, MODIFIED bigint, HASBINARY smallint, DELETEDONCE smallint, 
  			   MODCOUNT bigint, CMODCOUNT bigint, DSIZE bigint, VERSION smallint, SDTYPE smallint, SDMAXREVTIME bigint, 
  			   DATA nvarchar(4000), BDATA varbinary(max), constraint JOURNAL_PK primary key clustered (ID ASC))
  create index JOURNAL_MOD on JOURNAL (MODIFIED)
  create index JOURNAL_VSN on JOURNAL (VERSION)
  create index JOURNAL_SDT on JOURNAL (SDTYPE) where SDTYPE is not null
  create index JOURNAL_SDM on JOURNAL (SDMAXREVTIME) where SDMAXREVTIME is not null

  -- creating table NODES for schema version 2
  create table NODES (ID varbinary(512) not null, MODIFIED bigint, HASBINARY smallint, DELETEDONCE smallint, 
               MODCOUNT bigint, CMODCOUNT bigint, DSIZE bigint, VERSION smallint, SDTYPE smallint, SDMAXREVTIME bigint, 
               DATA nvarchar(4000), BDATA varbinary(max), constraint NODES_PK primary key clustered (ID ASC))
  create index NODES_MOD on NODES (MODIFIED)
  create index NODES_VSN on NODES (VERSION)
  create index NODES_SDT on NODES (SDTYPE) where SDTYPE is not null
  create index NODES_SDM on NODES (SDMAXREVTIME) where SDMAXREVTIME is not null

  -- creating table SETTINGS for schema version 2
  create table SETTINGS (ID varbinary(512) not null, MODIFIED bigint, HASBINARY smallint, DELETEDONCE smallint, 
  			   MODCOUNT bigint, CMODCOUNT bigint, DSIZE bigint, VERSION smallint, SDTYPE smallint, SDMAXREVTIME bigint, 
           DATA nvarchar(4000), BDATA varbinary(max), constraint SETTINGS_PK primary key clustered (ID ASC))
  create index SETTINGS_MOD on SETTINGS (MODIFIED)
  create index SETTINGS_VSN on SETTINGS (VERSION)
  create index SETTINGS_SDT on SETTINGS (SDTYPE) where SDTYPE is not null
  create index SETTINGS_SDM on SETTINGS (SDMAXREVTIME) where SDMAXREVTIME is not null

   -- creating blob store tables
  create table DATASTORE_META (ID varchar(64) not null primary key, LVL int, LASTMOD bigint)
  create table DATASTORE_DATA (ID varchar(64) not null, DATA varbinary(max)constraint DATASTORE_DATA_PK 
               primary key clustered (ID ASC))