# AWS IAM Group Management With Java

This repository is a study on automating the creation and deleting of AWS IAM Groups using the AWS SDK for Java 2.x.

### Overview

- **CreateIAMGroupsWithPolicies**: Automates the creation of groups and attaches predefined policies.
- **DeleteIAMGroups**: Deletes all groups from your AWS account.

### Requirements

- Java 1.8+
- AWS CLI configured with an authenticated user

### Usage

1. Clone the repository.
2. Open the project in Eclipse or your preferred IDE.

3. **Customize the Code:**
   - Edit the `groupNames` and `groupPolicies` arrays in the `CreateIAMGroupsWithPolicies` class to fit your needs. For example:

     ```java
     // Edit the `groupNames` array to add or remove groups.
     String[] groupNames = { "Developers", "Admins", "Support", "CloudAdmin" };

     // Edit the `groupPolicies` array to add or remove policies for each group.
     String[][] groupPolicies = {
             { "arn:aws:iam::aws:policy/PowerUserAccess", "arn:aws:iam::aws:policy/AmazonS3FullAccess" }, // Policies for Developers
             { "arn:aws:iam::aws:policy/AdministratorAccess" }, // Policies for Admins
             { "arn:aws:iam::aws:policy/IAMUserChangePassword" }, // Policies for Support
             { "arn:aws:iam::aws:policy/AdministratorAccess" } // Policies for CloudAdmin
     };
     ```

4. Run the classes as needed:
   - `CreateIAMGroupsWithPolicies`: Creates IAM Groups and assigns policies.
   - `DeleteIAMGroups`: Deletes all IAM Groups in your AWS account.

### Console Output
  - Created group
![Console Output](aws-iam-group/src/main/resources/img/log-console.PNG)
  - Deleted group
![Console Output](aws-iam-group/src/main/resources/img/log-console2.PNG)
