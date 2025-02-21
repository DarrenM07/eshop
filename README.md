# eshop
*ASSIGNMENT 1*
Reflection 1 
In developing the EShop application, I applied clean code principles such as meaningful naming conventions (e.g., productId), and followed the Single Responsibility Principle (SRP) by separating concerns between the controller, service, and repository layers. I also reduced code duplication by reusing logic within the service and repository. For secure coding, I added confirmation prompts for delete actions to prevent accidental deletions. Areas for improvement include adding input validation with annotations like @NotNull and @Size, implementing custom error pages for better error handling, and adding authorization checks to secure edit and delete endpoints. Moving forward, I plan to integrate unit testing with JUnit and connect the application to a database for persistent data storage.

Reflection 2
How do you feel after writing the unit test?
Honestly, writing unit tests can feel a bit tedious at first, but it's also kinda satisfying when everything runs without errors. It gives a sense of confidence that the code is actually working as expected. Plus, seeing all tests pass is lowkey addictive—feels like winning a mini battle in coding.

How many unit tests should be made in a class?
There’s no fixed number, but the idea is to test all possible scenarios—both positive (expected behavior) and negative (edge cases, invalid inputs, etc.). Every method that contains some logic should ideally have at least one test case, but if there are multiple conditions, we should test them all.

How do we make sure our unit tests are enough?
A good way to check is by using code coverage tools like JaCoCo. These tools show what percentage of the code is actually being tested. But having 100% coverage doesn’t mean the code is bug-free—it just means every line was executed during testing. We still need to consider edge cases, unexpected inputs, and real-world scenarios to make our tests meaningful.

*ASSIGNMENT 2*
During the exercise, I addressed several code quality issues, notably replacing the repeated literal string "redirect:/product/list" with a constant (REDIRECT_PRODUCT_LIST) to eliminate redundancy and improve maintainability. I also identified and filled test coverage gaps by adding tests for edge cases, such as when a product’s ID is null during the edit flow, ensuring that all controller methods have 100% code coverage. Moreover, the CI/CD pipeline is set up to automatically run test suites, analyze code quality, and deploy the application to a PaaS, which embodies the principles of Continuous Integration and Continuous Deployment. Every commit triggers these automated processes, catching issues early and ensuring that successful builds are seamlessly deployed to production without manual intervention, keeping the software in a reliably deployable state at all times.