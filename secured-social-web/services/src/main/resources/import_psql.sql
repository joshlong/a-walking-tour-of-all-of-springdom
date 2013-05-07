

-- this table is for Spring Social to manage social provider connections
CREATE TABLE UserConnection
(
  userid character varying(255) NOT NULL,
  providerid character varying(255) NOT NULL,
  provideruserid character varying(255) NOT NULL,
  rank integer NOT NULL,
  displayname character varying(255),
  profileurl character varying(512),
  imageurl character varying(512),
  accesstoken character varying(255) NOT NULL,
  secret character varying(255),
  refreshtoken character varying(255),
  expiretime bigint,
  CONSTRAINT userconnection_pkey PRIMARY KEY (userid , providerid , provideruserid )
);

CREATE TABLE UserAccount
(
  id bigint NOT NULL,
  enabled boolean NOT NULL,
  password character varying(255),
  importedFromServiceProvider boolean not null ,
  firstname character varying(255),
  lastname character varying(255),
  profilephotoext character varying(255),
  profilephotoimported boolean NOT NULL,
  signupdate timestamp without time zone,
  username character varying(255),
  CONSTRAINT users_pkey PRIMARY KEY (id )
) ; 


CREATE TABLE Customer
(
  id bigint NOT NULL,
  firstname character varying(255),
  lastname character varying(255),
  signupdate timestamp without time zone,
  user_id bigint NOT NULL,
  CONSTRAINT customers_pkey PRIMARY KEY (id ),
  CONSTRAINT user_fkey FOREIGN KEY (user_id)
      REFERENCES UserAccount (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
) ;