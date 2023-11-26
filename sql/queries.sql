--Modifying Data--------------------------------------------------------------------------------------------------------

--Question 1: Insert some data into a table
INSERT INTO cd.facilities 
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) VALUES
(9, 'Spa', 20, 30, 100000, 800);

--Question 2: Insert calculated data into a table
INSERT INTO cd.facilities 
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) VALUES
((SELECT MAX(facid) FROM cd.facilities)+1,'Spa', 20, 30, 100000, 800);

--Question 3: Update some existing data
UPDATE cd.facilities
SET initialoutlay = 8000
WHERE name='Tennis Court 2';

--Question 4: Update a row based on the contents of another row
UPDATE cd.facilities
SET initialoutlay = initialoutlay*1.10
WHERE name='Tennis Court 2';

--Question 5: Delete all bookings
TRUNCATE TABLE cd.bookings;

--Question 6: Delete a member from the cd.members table
DELETE FROM cd.members
WHERE memid=37;

--Basics----------------------------------------------------------------------------------------------------------------

--Question 7: Control which rows are retrieved - part 2
SELECT *
FROM cd.facilities
WHERE membercost < monthlymaintenance/50;

--Question 8: Basic string searches
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';

--Question 9: Matching against multiple possible values
SELECT *
FROM cd.facilities
WHERE facid IN (1,5);

--Question 10: Working with dates
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01'

--Question 11: Combining results from multiple queries
SELECT DISTINCT surname
FROM cd.members
UNION
SELECT DISTINCT name AS surname
FROM cd.facilities;

--Join------------------------------------------------------------------------------------------------------------------

--Question 12: Retrieve the start times of members' bookings
SELECT starttime
FROM cd.bookings
INNER JOIN cd.members
ON cd.bookings.memid = cd.members.memid
WHERE firstname = 'David' AND surname = 'Farrell';

--Question 13: Work out the start times of bookings for tennis courts
SELECT starttime AS start, name
FROM cd.bookings
INNER JOIN cd.facilities
ON cd.bookings.facid = cd.facilities.facid
WHERE starttime::date = '2012-09-21' AND name LIKE '%Tennis Court%';

--Question 14: Produce a list of all members, along with their recommender
SELECT A.firstname AS memfname, A.surname AS memsname, B.firstname AS recfname, B.surname AS recsname
FROM cd.members AS A
LEFT JOIN cd.members AS B
ON A.recommendedby = B.memid OR A.recommendedby = null
ORDER BY A.surname, A.firstname;

--Question 15: Produce a list of all members who have recommended another member
SELECT firstname, surname
FROM cd.members
WHERE memid IN (SELECT recommendedby FROM cd.members WHERE recommendedby IS NOT NULL)
ORDER BY surname, firstname;

--Question 16: Produce a list of all members, along with their recommender, using no joins.
SELECT A.firstname || ' ' || A.surname AS member, 
(
    SELECT B.firstname || ' ' || B.surname AS recommender 
    FROM cd.members AS B
    WHERE A.recommendedby = B.memid
)
FROM cd.members AS A
ORDER BY A.firstname, A.surname;

--Aggregation-----------------------------------------------------------------------------------------------------------

--Question 17: Count the number of recommendations each member makes.
SELECT recommendedby, COUNT(recommendedby) AS count
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER by recommendedby;

--Question 18: List the total slots booked per facility
SELECT facid, SUM(slots) AS Total_Slots
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

--Question 19: List the total slots booked per facility in a given month
SELECT facid, SUM(slots) AS Total_Slots
FROM cd.bookings
WHERE starttime::date >= '2012-09-01' AND starttime::date < '2012-10-01'
GROUP BY facid
ORDER BY Total_Slots;

--Question 20: List the total slots booked per facility per month
SELECT facid, EXTRACT(MONTH FROM starttime) AS month, SUM(slots) AS Total_Slots
FROM cd.bookings
WHERE EXTRACT(YEAR FROM starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;

--Question 21: Find the count of members who have made at least one booking
SELECT COUNT(DISTINCT memid) AS count
FROM cd.members
WHERE memid IN (SELECT memid FROM cd.bookings);

--Question 22: List each member's first booking after September 1st 2012
SELECT DISTINCT surname, firstname, cd.members.memid, MIN(starttime)
FROM cd.members
LEFT JOIN cd.bookings
ON cd.members.memid = cd.bookings.memid
WHERE starttime::date >= '2012-09-01'
GROUP BY cd.members.memid
ORDER BY cd.members.memid;

--Question 23: Produce a list of member names, with each row containing the total member count
SELECT (SELECT COUNT(*) FROM cd.members), firstname, surname
FROM cd.members
GROUP BY memid
ORDER BY joindate;

--Question 24: Produce a numbered list of members
SELECT ROW_NUMBER() OVER(ORDER BY joindate) AS row, firstname, surname
FROM cd.members;

--Question 25: Output the facility id that has the highest number of slots booked, again
--https://www.2ndquadrant.com/en/blog/postgresql-13-limit-with-ties/ (Requires PostgreSQL 13 and for some reason won't work)
SELECT facid, SUM(slots) AS total
FROM cd.bookings
GROUP BY facid
ORDER BY SUM(SLOTS) DESC
FETCH FIRST 1 ROWS WITH TIES;

--String----------------------------------------------------------------------------------------------------------------

--Question 26: Format the names of members
SELECT surname || ', ' || firstname AS name
FROM cd.members;

--Question 27: Find telephone numbers with parentheses
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(%'
ORDER BY memid;

--Question 28: Count the number of members whose surname starts with each letter of the alphabet
SELECT SUBSTRING(surname, 1, 1) AS letter, COUNT(*) AS count
FROM cd.members
GROUP BY letter
ORDER BY letter;