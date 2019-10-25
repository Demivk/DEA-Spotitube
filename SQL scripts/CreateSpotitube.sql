USE master
GO

-- Deleting Spotitube if exists --
IF DB_ID('Spotitube') IS NOT NULL
BEGIN
	RAISERROR('Deleting database Spotitube', 0, 1) WITH NOWAIT
	ALTER DATABASE Spotitube SET SINGLE_USER WITH ROLLBACK IMMEDIATE
	DROP DATABASE Spotitube
END

-- Creating the database --
RAISERROR('Creating database Spotitube...', 0, 1) WITH NOWAIT
CREATE DATABASE Spotitube
RAISERROR('Created database Spotitube', 0, 1) WITH NOWAIT
GO

USE Spotitube

CREATE TABLE [User] (
	[user]				VARCHAR(25)		NOT NULL,
	[password]			VARCHAR(32)		NOT NULL,

	CONSTRAINT pk_User PRIMARY KEY ([user])
);


CREATE TABLE [Authentication] (
	[user]				VARCHAR(25)		NOT NULL,
	token				VARCHAR(20)		NOT NULL,

	CONSTRAINT pk_Authentication PRIMARY KEY ([user], token),
	CONSTRAINT fk_Authentication_User FOREIGN KEY ([user]) REFERENCES [User] ([user])
		ON UPDATE NO ACTION
		ON DELETE NO ACTION,
	CONSTRAINT uq_AuthenticationToken UNIQUE (token),
	CONSTRAINT uq_AuthenticationUser UNIQUE ([user])
);

CREATE TABLE Playlist (
	id					INTEGER			NOT NULL,
	[name]				VARCHAR(30)		NOT NULL,
	[owner]				BIT				NOT NULL	DEFAULT(0),

	CONSTRAINT pk_Playlist PRIMARY KEY (id)
);

CREATE TABLE Track (
	id					INTEGER			NOT NULL,
	title				VARCHAR(75)		NOT NULL,
	performer			VARCHAR(75)		NOT NULL,
	duration			INTEGER			NOT NULL,
	album				VARCHAR(50)			NULL	DEFAULT('undefined'),
	playcount			INTEGER				NULL	DEFAULT(0),
	publicationdate		DATE				NULL	DEFAULT('undefined'),
	[description]		VARCHAR(100)		NULL	DEFAULT('undefined'),
	offlineAvailable	BIT				NOT NULL	DEFAULT(0),

	CONSTRAINT pk_Track PRIMARY KEY (id),
	CONSTRAINT u_Track UNIQUE(title, performer)
);

CREATE TABLE PlaylistTrack (
	playlistId			INTEGER			NOT NULL,
	trackId				INTEGER			NOT NULL,

	CONSTRAINT pk_PlaylistTrack PRIMARY KEY (playlistId, trackId),
	CONSTRAINT fk_PlaylistTrack_Playlist FOREIGN KEY (playlistId) REFERENCES Playlist(id)
		ON UPDATE NO ACTION
		ON DELETE NO ACTION,
	CONSTRAINT fk_PlaylistTrack_Tracks FOREIGN KEY (trackId) REFERENCES Track(id)
		ON UPDATE NO ACTION
		ON DELETE NO ACTION
);

CREATE TABLE PlaylistUser (
	[user]				VARCHAR(25)		NOT NULL,
	playlistId			INTEGER			NOT NULL,

	CONSTRAINT pk_PlaylistUser PRIMARY KEY ([user], playlistId),
	CONSTRAINT fk_PlaylistUser_User FOREIGN KEY ([user]) REFERENCES [User]([user])
		ON UPDATE NO ACTION
		ON DELETE NO ACTION,
	CONSTRAINT fk_PlaylistUser_Playlist FOREIGN KEY (playlistId) REFERENCES Playlist(id)
		ON UPDATE NO ACTION
		ON DELETE NO ACTION
);

RAISERROR('Created tables in database Spotitube', 0, 1) WITH NOWAIT