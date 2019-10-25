USE Spotitube

DELETE FROM PlaylistUser
DELETE FROM PlaylistTrack
DELETE FROM Track
DELETE FROM Playlist
DELETE FROM [Authentication]
DELETE FROM [User]

INSERT INTO [User]
VALUES	('Demi', 'password123'),
		('Tester', 'test');

INSERT INTO [Authentication]
VALUES	('Demi', '1234-1234-1234'),
		('Tester', '5678-5678-5678');

INSERT INTO Playlist /*playlistId, name, owner*/
VALUES	(1, 'Pop', 1),
		(2, 'Christmas', 0),
		(3, 'Testlist', 1);

INSERT INTO Track /* Track: performer, titel, duration. Song: track + album. Video: track + publicationdate, description */
VALUES	(1, 'Burn the House Down',		'AJR',				213, 'The Click',			   15, NULL,		  NULL,					0),
		(2, 'All I Want For Christmas', 'Mariah Carey',		235, 'Merry Christmas',		    1, NULL,		  NULL,					0),
		(3, 'IDOL',						'BTS',				223, 'Love Yourself - Answer', 50, '24-aug-2018', 'Official Videoclip', 1),
		(4, 'Where Have You Gone',		'Lucas & Steve',	165, 'Where Have You Gone',		5, '28-sep-2018', 'Offical Music Video',1),
		(5, 'Siren',					'The Chainsmokers', 174, 'Sick Boy',			   11, NULL,		  NULL,					1),
		(6, 'breathin',					'Ariana Grande',	198, 'Sweetener',			   17, NULL,		  NULL,					0),
		(7, 'Waiting For Tomorrow',		'Martin Garrix',	248, 'Waiting For Tomorrow',    7, '19-oct-2018', 'feat. Mike Shinoda', 1);

INSERT INTO PlaylistTrack /*playlistId, trackId*/
VALUES	(1, 1),
		(2, 2),
		(1, 3),
		(3, 4);

INSERT INTO PlaylistUser /*user, playlistId*/
VALUES	('Demi',   1),
		('Demi',   2),
		('Tester', 3);

SELECT * FROM [User]
SELECT * FROM [Authentication]
SELECT * FROM Playlist
SELECT * FROM Track
SELECT * FROM PlaylistTrack
SELECT * FROM PlaylistUser