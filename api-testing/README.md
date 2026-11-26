API Testing

This folder contains the API testing files for the Frame Match project.

Backend URL

http://localhost:8082

Testing Tool

Postman

Tested API Endpoints

Method	Endpoint	Description
GET	/api/frames	Get all glasses frames
GET	/api/faceshapes	Get all face shapes
GET	/api/matches	Get face shape and frame matches
GET	/api/face-analysis/recommend/{id}	Get recommended frames
GET	/api/admins/{id}	Get admin by ID
POST	/api/admins	Create a new admin
DELETE	/api/admins/{id}	Delete an admin

Postman Collection

Frame-Match-API.postman_collection.json

Note

The backend must be running before testing the endpoints.
Admin endpoints require Basic Authentication.
