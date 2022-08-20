-- assumption is that the journeys could change
- not specified date in tests as today's date is taken to avoid the tests being brittle and broken

and negative test - date in the past

- The results could vary consistently so assert against exact values at low level would make the tests brittle. Better to assert against schema and that the results returned are not empty based on the journey modes