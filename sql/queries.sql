--Modifying Data--------------------------------------------------------------------------------------------------------

--Question 1: Insert some data into a table
--https://pgexercises.com/questions/updates/insert.html
INSERT INTO cd.facilities 
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) VALUES
(9, 'Spa', 20, 30, 100000, 800);

--Question 2: Insert calculated data into a table
--https://pgexercises.com/questions/updates/insert3.html
INSERT INTO cd.facilities 
(facid, name, membercost, guestcost, initialoutlay, monthlymaintenance) VALUES
((SELECT MAX(facid) FROM cd.facilities)+1,'Spa', 20, 30, 100000, 800);

--Question 3: Update some existing data
--https://pgexercises.com/questions/updates/update.html
UPDATE cd.facilities
SET initialoutlay = 8000
WHERE name='Tennis Court 2';

--Question 4: Update a row based on the contents of another row
--https://pgexercises.com/questions/updates/updatecalculated.html
UPDATE cd.facilities
SET initialoutlay = initialoutlay*1.10
WHERE name='Tennis Court 2';

--Question 5: Delete all bookings
--https://pgexercises.com/questions/updates/delete.html
TRUNCATE TABLE cd.bookings;

--Question 6: Delete a member from the cd.members table
--https://pgexercises.com/questions/updates/deletewh.html
DELETE FROM cd.members
WHERE memid=37;

--Basics----------------------------------------------------------------------------------------------------------------

--Question 7: Control which rows are retrieved - part 2
--https://pgexercises.com/questions/basic/where2.html
SELECT *
FROM cd.facilities
WHERE membercost < monthlymaintenance/50;

--Question 8: Basic string searches
--https://pgexercises.com/questions/basic/where3.html
SELECT *
FROM cd.facilities
WHERE name LIKE '%Tennis%';

--Question 9: Matching against multiple possible values
--https://pgexercises.com/questions/basic/where4.html
SELECT *
FROM cd.facilities
WHERE facid IN (1,5);

--Question 10: Working with dates
--https://pgexercises.com/questions/basic/date.html
SELECT memid, surname, firstname, joindate
FROM cd.members
WHERE joindate >= '2012-09-01'

--Question 11: Combining results from multiple queries
--https://pgexercises.com/questions/basic/union.html
SELECT DISTINCT surname
FROM cd.members
UNION
SELECT DISTINCT name AS surname
FROM cd.facilities;

--Join------------------------------------------------------------------------------------------------------------------

--Question 12: Retrieve the start times of members' bookings
--https://pgexercises.com/questions/joins/simplejoin.html
SELECT starttime
FROM cd.bookings
INNER JOIN cd.members
ON cd.bookings.memid = cd.members.memid
WHERE firstname = 'David' AND surname = 'Farrell';

--Question 13: Work out the start times of bookings for tennis courts
--https://pgexercises.com/questions/joins/simplejoin2.html
SELECT starttime AS start, name
FROM cd.bookings
INNER JOIN cd.facilities
ON cd.bookings.facid = cd.facilities.facid
WHERE starttime::date = '2012-09-21' AND name LIKE '%Tennis Court%';

--Question 14: Produce a list of all members, along with their recommender
--https://pgexercises.com/questions/joins/self2.html
SELECT A.firstname AS memfname, A.surname AS memsname, B.firstname AS recfname, B.surname AS recsname
FROM cd.members AS A
LEFT JOIN cd.members AS B
ON A.recommendedby = B.memid OR A.recommendedby = null
ORDER BY A.surname, A.firstname;

--Question 15: Produce a list of all members who have recommended another member
--https://pgexercises.com/questions/joins/self.html
SELECT firstname, surname
FROM cd.members
WHERE memid IN (SELECT recommendedby FROM cd.members WHERE recommendedby IS NOT NULL)
ORDER BY surname, firstname;

--Question 16: Produce a list of all members, along with their recommender, using no joins.
--https://pgexercises.com/questions/joins/sub.html
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
--https://pgexercises.com/questions/aggregates/count3.html
SELECT recommendedby, COUNT(recommendedby) AS count
FROM cd.members
WHERE recommendedby IS NOT NULL
GROUP BY recommendedby
ORDER by recommendedby;

--Question 18: List the total slots booked per facility
--https://pgexercises.com/questions/aggregates/fachours.html
SELECT facid, SUM(slots) AS Total_Slots
FROM cd.bookings
GROUP BY facid
ORDER BY facid;

--Question 19: List the total slots booked per facility in a given month
--https://pgexercises.com/questions/aggregates/fachoursbymonth.html
SELECT facid, SUM(slots) AS Total_Slots
FROM cd.bookings
WHERE starttime::date >= '2012-09-01' AND starttime::date < '2012-10-01'
GROUP BY facid
ORDER BY Total_Slots;

--Question 20: List the total slots booked per facility per month
--https://pgexercises.com/questions/aggregates/fachoursbymonth2.html
SELECT facid, EXTRACT(MONTH FROM starttime) AS month, SUM(slots) AS Total_Slots
FROM cd.bookings
WHERE EXTRACT(YEAR FROM starttime) = 2012
GROUP BY facid, month
ORDER BY facid, month;

--Question 21: Find the count of members who have made at least one booking
--https://pgexercises.com/questions/aggregates/members1.html
SELECT COUNT(DISTINCT memid) AS count
FROM cd.members
WHERE memid IN (SELECT memid FROM cd.bookings);

--Question 22: List each member's first booking after September 1st 2012
--https://pgexercises.com/questions/aggregates/nbooking.html
SELECT DISTINCT surname, firstname, cd.members.memid, MIN(starttime)
FROM cd.members
LEFT JOIN cd.bookings
ON cd.members.memid = cd.bookings.memid
WHERE starttime::date >= '2012-09-01'
GROUP BY cd.members.memid
ORDER BY cd.members.memid;

--Question 23: Produce a list of member names, with each row containing the total member count
--https://pgexercises.com/questions/aggregates/countmembers.html
SELECT (SELECT COUNT(*) FROM cd.members), firstname, surname
FROM cd.members
GROUP BY memid
ORDER BY joindate;

--Question 24: Produce a numbered list of members
--https://pgexercises.com/questions/aggregates/nummembers.html
SELECT ROW_NUMBER() OVER(ORDER BY joindate) AS row, firstname, surname
FROM cd.members;

--Question 25: Output the facility id that has the highest number of slots booked, again
--https://pgexercises.com/questions/aggregates/fachours4.html
--https://www.2ndquadrant.com/en/blog/postgresql-13-limit-with-ties/ (Requires PostgreSQL 13 and for some reason won't work)
SELECT facid, SUM(slots) AS total
FROM cd.bookings
GROUP BY facid
ORDER BY SUM(SLOTS) DESC
FETCH FIRST 1 ROWS WITH TIES;

--String----------------------------------------------------------------------------------------------------------------

--Question 26: Format the names of members
--https://pgexercises.com/questions/string/concat.html
SELECT surname || ', ' || firstname AS name
FROM cd.members;

--Question 27: Find telephone numbers with parentheses
--https://pgexercises.com/questions/string/reg.html
SELECT memid, telephone
FROM cd.members
WHERE telephone LIKE '%(%'
ORDER BY memid;

--Question 28: Count the number of members whose surname starts with each letter of the alphabet
--https://pgexercises.com/questions/string/substr.html
SELECT SUBSTRING(surname, 1, 1) AS letter, COUNT(*) AS count
FROM cd.members
GROUP BY letter
ORDER BY letter;