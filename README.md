# assignment
This convenience service provides a (simplified) train timetable with weekday train times leaving Union Station.

So far, it only supports a single request, GET /schedule, which returns all trains from all lines. The response format includes the line (Lakeshore, Barrie or Kitchener) and the departure and arrival times, in 24-hour format: 900 means 9am, 1330 means 1:30pm, etc.

[
    {
        "id": 1,
        "line": "Lakeshore",
        "departure": 800,
        "arrival": 900
    },
    {
        "id": 2,
        "line": "Lakeshore",
        "departure": 1000,
        "arrival": 1100
    },
    { "...": "..." }
]
The full timetable is stored into an in-memory database (H2) and initialized at application start time. You can see all the data in the data.sql script under src\main\resources.

We believe you can improve this service with the changes suggested below. Complete as many of those as you can within the assessment time.

1. Add an endpoint to query by line and departure time
Implement an additional endpoint with the following format: GET /schedule/{line}?departure={time}. The departure time is optional and, for now, you can keep using the 24-hour format. If a line does not have any trains departing at the specified time, the request should still be successful, but should return an empty array.

2. Improve the HTTP response codes for the new endpoint
For the /schedule/{line} endpoint implemented above, make sure the service will:

Respond with HTTP 404 (Not Found) when you search for a non-existent train line. It should still respond with an empty array if the line exists but the departure time does not.
Respond with HTTP 400 (Bad Request) if the departure parameter is invalid.
Bonus points if you implement your own unit tests for this functionality.

3. Improve the time format accepted by the search endpoint
Make the service accept departure times in am/pm format, with hours and minutes separated by a colon, e.g. GET /schedule/Lakeshore?departure=2:00pm should return the train scheduled for 14:00 hours:

[    
  {
      "id": 4,
      "line": "Lakeshore",
      "departure": 1400,
      "arrival": 1600
  }
]
For simplicity, the colon and the "am/pm" bits are mandatory, so times like 6am or 11:00 should be rejected with a HTTP 400 (Bad Request) response. The validation implemented previously should be preserved as well.

Bonus points if you implement your own unit tests for this functionality.

4. Improve API performance with a cache
As we expect a high number of calls to this service, enable caching for the requests in a way where the DB is not queried twice for a repeat request.

Bonus points if you add an automated test for this feature.

[execution time limit] 30 seconds
