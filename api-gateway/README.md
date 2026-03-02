API Gateway project (Spring Cloud Gateway)
-----------------------------------------

- Port: 8085
- Registers with Eureka at http://localhost:8761
- Routes defined:
  - /students/** -> lb://student-service
  - /school/**   -> lb://school-service

Steps:
1. Start Eureka server on port 8761.
2. Make sure student-service and school-service are running and registered.
3. Run: mvn spring-boot:run
4. Test: curl http://localhost:8085/students
