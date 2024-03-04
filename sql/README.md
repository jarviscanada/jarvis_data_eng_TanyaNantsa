# Introduction  
The project focuses on facilitating the learning of SQL (Structured Query Language) and relational database management system (RDBMS) skills. The main goal of the project is to provide a platform for learning and practicing SQL through the query-solving, while also learning basic data modelling principles and SQL optimization. The project is designed as a learning activity to support individuals in acquiring essential SQL and RDBMS knowledge, a skill crucial for roles involving data interaction. Technologies employed in the project include PostgreSQL for creating/modifying databases and running queries on them and Git for project version management.  

# SQL Quries

###### Table Setup (DDL)
```sql
-- Create `members` table  
CREATE TABLE cd.members (  
    memid INT NOT NULL,  
    surname VARCHAR(200) NOT NULL,  
    firstname VARCHAR(200) NOT NULL,  
    address VARCHAR(300) NOT NULL,  
    zipcode INT NOT NULL,  
    telephone VARCHAR(20) NOT NULL,  
    recommendedby INT,  
    joindate TIMESTAMP NOT NULL,  
    CONSTRAINT members_pk PRIMARY KEY (memid),  
    CONSTRAINT members_fk FOREIGN KEY (recommendedby) REFERENCES cd.members(memid) ON DELETE SET NULL  
);  

-- Create `bookings` table  
CREATE TABLE cd.bookings (  
	bookid INT NOT NULL,  
	facid INT NOT NULL,  
	memid INT NOT NULL,  
	starttime TIMESTAMP NOT NULL,  
	slots INT NOT NULL,  
	CONSTRAINT bookings_pk PRIMARY KEY (bookid),  
    CONSTRAINT bookings_fk FOREIGN KEY (facid) references cd.facilities(facid),  
    CONSTRAINT bookings_fk FOREIGN KEY (memid) references cd.members(memid)  
);  

-- Create `facilities` table  
CREATE TABLE cd.facilities (  
	facid INT NOT NULL,  
	name VARCHAR(100) NOT NULL,  
	membercost numeric NOT NULL,  
	guestcost numeric NOT NULL,  
	initialoutlay numeric NOT NULL,  
	monthlymaintenence numeric NOT NULL,  
	CONSTRAINT facilities_pk PRIMARY KEY (facid)  
);  
```

###### Question 1: Insert some data into a table  
```sql
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) 
VALUES (9, 'Spa', 20, 30, 100000, 800); 
```  

###### Question 2: Insert calculated data into table  
```sql
Insert calculated data into a table
INSERT INTO cd.facilities (facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) 
VALUES ((select max(facid) from cd.facilities)+1, 'Spa', 20, 30, 100000, 800);
```  

###### Questions 3: Update some existing data  
```sql
UPDATE cd.facilities
SET initialoutlay = 10000
WHERE initialoutlay = 8000;
```  

###### Question 4: Update a row based on the contents of another row  
```sql
UPDATE cd.facilities 
SET guestcost = (SELECT guestcost*1.1 FROM cd.facilities WHERE facid = 0),
membercost = (SELECT membercost*1.1 FROM cd.facilities WHERE facid = 0)
WHERE facid = 1;
```  

###### Question 5: Delete all bookings
```sql
TRUNCATE cd.bookings;
```

###### Question 6: Delete a member from the cd.members table  
```sql
DELETE FROM cd.members 
WHERE memid = 37;
```  

###### Question 7: Control which rows are retrieved
```sql
SELECT facid, name, membercost, monthlymaintenance
FROM cd.facilities
WHERE membercost < monthlymaintenance/50 
AND membercost > 0; 
```  

###### Question 8: Basic string searches
```sql
SELECT * FROM cd.facilities
WHERE name like '%Tennis%';
```

###### Question 9: Matching against multiple possible values
```sql
SELECT * FROM cd.facilities
WHERE facid in (1,5);
```

###### Question 10: Working with dates
```sql
SELECT memid, surname, firstname, joindate 
FROM cd.members
WHERE joindate >= '2012-09-01';
```

###### Question 11: Combining results from multiple queries
```sql
SELECT surname FROM cd.members 
UNION 
SELECT name FROM cd.facilities;
```  

###### Question 12: Retrieve the start times of members' bookings
```sql
SELECT starttime FROM cd.bookings bks
JOIN cd.members mem
ON mem.memid=bks.memid
WHERE mem.firstname = 'David' and mem.surname = 'Farrell';
```  

###### Question 13: Work out the start times of bookings for tennis courts
```sql
SELECT bks.starttime, faci.name
FROM cd.bookings bks
JOIN cd.facilities faci
ON faci.facid = bks.facid
WHERE faci.name in ('Tennis Court 2','Tennis Court 1') and
bks.starttime >= '2012-09-21' and
bks.starttime < '2012-09-22'
ORDER BY bks.starttime;
```

###### Question 14: Produce a list of all members, along with their recommender
```sql
SELECT mem.firstname memfn, mem.surname memsn, rec.firstname recfn, rec.surname recsn
FROM cd.members mem
LEFT JOIN cd.members rec
ON rec.memid=mem.recommendedby
ORDER BY memsn, memfn;
```  

###### Question 15: Produce a list of all members who have recommended another member
```sql
SELECT DISTINCT rec.firstname, rec.surname
FROM cd.members mem
INNER JOIN cd.members rec
ON mem.recommendedby = rec.memid
ORDER BY rec.surname, rec.firstname;
```  

###### Question 15: Produce a list of all members, along with their recommender, using no joins
```sql
SELECT DISTINCT CONCAT(firstname,' ',surname) name, 
    (SELECT CONCAT(firstname,' ',surname) name2 
    FROM cd.members 
    WHERE memid=mem.recommendedby
    )
FROM cd.members mem
ORDER BY name;
```  

###### Question 16: Count the number of recommendations each member makes
```sql
SELECT recommendedby, COUNT(*)
FROM cd.members 
WHERE recommendedby is NOT NULL
GROUP BY recommendedby
ORDER BY recommendedby;
```  

###### Question 17: List the total slots booked per facility
```sql
SELECT facid, SUM(slots)
FROM cd.bookings
GROUP BY facid
ORDER BY facid;
```  

###### Question 18: List the total slots booked per facility in a given month
```sql
SELECT facid, sum(slots)
FROM cd.bookings
WHERE starttime<'2012-10-01' and starttime>'2012-09-01'
GROUP BY facid
ORDER BY sum(slots);
```  

###### Question 19: List the total slots booked per facility per month
```sql
SELECT facid, extract(month from starttime) as month, sum(slots)
FROM cd.bookings
WHERE extract(year from starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month; 
```

###### Question 20: Find the count of members who have made at least one booking
```sql
SELECT count(distinct memid) 
FROM cd.bookings;
```

###### Question 21: List each member's first booking after September 1st 2012
```sql
SELECT mems.surname, mems.firstname, mems.memid, min(bks.starttime) as starttime
FROM cd.bookings bks
INNER JOIN cd.members mems 
ON mems.memid = bks.memid
WHERE starttime >= '2012-09-01'
GROUP BY mems.surname, mems.firstname, mems.memid
ORDER BY mems.memid; 
```

###### Question 22: Produce a list of member names, with each row containing the total member count
```sql
SELECT (SELECT count(*) FROM cd.members) AS count, firstname, surname
FROM cd.members
ORDER BY joindate;
```

###### Question 23: Produce a numbered list of members
```sql
SELECT row_number() OVER(ORDER BY joindate), firstname, surname
FROM cd.members
ORDER BY joindate;
```

###### Question 24: Output the facility id that has the highest number of slots booked
```sql
SELECT facid, total FROM (
SELECT facid, sum(slots) total, rank() OVER (ORDER BY sum(slots) desc) rank
FROM cd.bookings
GROUP BY facid
) AS ranked
WHERE rank = 1 ;
```

###### Question 25: Format the names of members
```sql
SELECT surname || ', ' || firstname AS name FROM cd.members;
```

###### Question 26: Find telephone numbers with parentheses
```sql
SELECT memid, telephone
FROM cd.members
WHERE telephone ~ '[()]'
ORDER BY memid;
```

###### Question 27: Count the number of members whose surname starts with each letter of the alphabet
```sql
SELECT substr (surname,1,1) as letter, count(*) as count 
FROM cd.members
GROUP BY letter
ORDER BY letter; 
```
