-- MODIFYING DATA QUESTIONS
-- Insert some data into a table
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) 
VALUES (9, 'Spa', 20, 30, 100000, 800);

-- Insert calculated data into a table
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) 
VALUES ((select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800);

-- Update some existing data
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE initialoutlay = 8000;

-- Update a row based on the contents of another row
UPDATE cd.facilities 
SET guestcost = (SELECT guestcost*1.1 FROM cd.facilities WHERE facid = 0),
membercost = (SELECT membercost*1.1 FROM cd.facilities WHERE facid = 0)
WHERE facid = 1;

-- Delete all bookings
TRUNCATE cd.bookings;

-- Delete a member from the cd.members table
DELETE FROM cd.members 
WHERE memid = 37;


-- BASIC QUESTIONS
-- Control which rows are retrieved - part 2
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance/50 
AND membercost > 0;

-- Basic string searches
SELECT * FROM cd.facilities
WHERE name like '%Tennis%';

-- Matching against multiple possible values
SELECT * FROM cd.facilities
WHERE facid in (1,5);

-- Working with dates
SELECT memid, surname, firstname, joindate 
FROM cd.members
WHERE joindate >= '2012-09-01';

-- Combining results from multiple queries
SELECT surname FROM cd.members 
UNION 
SELECT name FROM cd.facilities;

--JOIN QUESTIONS
-- Retrieve the start times of members' bookings
SELECT starttime FROM cd.bookings bks
JOIN cd.members mem
ON mem.memid=bks.memid
WHERE mem.firstname = 'David' and mem.surname = 'Farrell';

-- Work out the start times of bookings for tennis courts
SELECT bks.starttime, faci.name
FROM cd.bookings bks
JOIN cd.facilities faci
ON faci.facid = bks.facid
WHERE faci.name in ('Tennis Court 2','Tennis Court 1') and
bks.starttime >= '2012-09-21' and
bks.starttime < '2012-09-22'
ORDER BY bks.starttime;

-- Produce a list of all members, along with their recommender
SELECT mem.firstname memfn, mem.surname memsn, rec.firstname recfn, rec.surname recsn
FROM cd.members mem
LEFT JOIN cd.members rec
ON rec.memid=mem.recommendedby
ORDER BY memsn, memfn;

-- Produce a list of all members who have recommended another member
SELECT DISTINCT rec.firstname, rec.surname
FROM cd.members mem
INNER JOIN cd.members rec
ON mem.recommendedby = rec.memid
ORDER BY rec.surname, rec.firstname;

-- Produce a list of all members, along with their recommender, using no joins
SELECT DISTINCT CONCAT(firstname,' ',surname) name, 
    (SELECT CONCAT(firstname,' ',surname) name2 
    FROM cd.members 
    WHERE memid=mem.recommendedby
    )
FROM cd.members mem
ORDER BY name;

--AGGREGATION QUESTIONS
-- Count the number of recommendations each member makes
SELECT recommendedby, COUNT(*)
FROM cd.members 
WHERE recommendedby is NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;

-- List the total slots booked per facility
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

-- List the total slots booked per facility in a given month
SELECT facid, sum(slots)
FROM cd.bookings
WHERE starttime<'2012-10-01' and starttime>'2012-09-01'
GROUP BY facid
ORDER BY sum(slots);

-- List the total slots booked per facility per month
SELECT facid, extract(month from starttime) as month, sum(slots)
FROM cd.bookings
WHERE extract(year from starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month; 

-- Find the count of members who have made at least one booking
SELECT count(distinct memid) 
FROM cd.bookings;

-- List each member's first booking after September 1st 2012
SELECT mems.surname, mems.firstname, mems.memid, min(bks.starttime) as starttime
FROM cd.bookings bks
INNER JOIN cd.members mems 
ON mems.memid = bks.memid
WHERE starttime >= '2012-09-01'
GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY mems.memid; 

-- Produce a list of member names, with each row containing the total member count
SELECT (SELECT count(*) FROM cd.members) AS count, firstname, surname
FROM cd.members
ORDER BY joindate

-- Produce a numbered list of members
SELECT row_number() OVER(ORDER BY joindate), firstname, surname
FROM cd.members
ORDER BY joindate

-- Output the facility id that has the highest number of slots booked
SELECT facid, total FROM (
SELECT facid, sum(slots) total, rank() OVER (ORDER BY sum(slots) desc) rank
FROM cd.bookings
GROUP BY facid
) AS ranked
WHERE rank = 1 

-- STRING QUESTIONS
-- Format the names of members
SELECT surname || ', ' || firstname AS name FROM cd.members

-- Find telephone numbers with parentheses
SELECT memid, telephone
FROM cd.members
WHERE telephone ~ '[()]'
ORDER BY memid;

-- Count the number of members whose surname starts with each letter of the alphabet
SELECT substr (surname,1,1) as letter, count(*) as count 
FROM cd.members
GROUP BY letter
ORDER BY letter; 