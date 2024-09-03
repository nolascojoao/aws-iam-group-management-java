package com.example;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.AttachGroupPolicyRequest;
import software.amazon.awssdk.services.iam.model.CreateGroupRequest;
import software.amazon.awssdk.services.iam.model.CreateGroupResponse;
import software.amazon.awssdk.services.iam.model.IamException;

public class CreateIAMGroupsWithPolicies {

	public static void main(String[] args) {

		Region region = Region.AWS_GLOBAL;

		IamClient iamClient = IamClient.builder().region(region)
				.credentialsProvider(ProfileCredentialsProvider.create()).build();

		String[] groupNames = { "Developers", "Admins", "Support", "CloudAdmin" };

		String[][] groupPolicies = {
				{ "arn:aws:iam::aws:policy/PowerUserAccess", "arn:aws:iam::aws:policy/AmazonS3FullAccess" }, // Policies for Developers
				{ "arn:aws:iam::aws:policy/AdministratorAccess" }, // Policies for Admins
				{ "arn:aws:iam::aws:policy/IAMUserChangePassword" }, // Policies for Support
				{ "arn:aws:iam::aws:policy/AdministratorAccess" } // Policies for CloudAdmin
		};

		for (int i = 0; i < groupNames.length; i++) {
			String groupName = groupNames[i];
			createIAMGroupWithPolicies(iamClient, groupName, groupPolicies[i]);
		}

		iamClient.close();
	}

	public static void createIAMGroupWithPolicies(IamClient iamClient, String groupName, String[] policies) {
		try {
			CreateGroupRequest createGroupRequest = CreateGroupRequest.builder().groupName(groupName).build();

			CreateGroupResponse createGroupResponse = iamClient.createGroup(createGroupRequest);
			System.out.println("Successfully created group: " + createGroupResponse.group().groupName());

			for (String policyArn : policies) {
				attachPolicyToGroup(iamClient, groupName, policyArn);
			}

		} catch (IamException e) {
			System.err.println("Failed to create group " + groupName + ": " + e.getMessage());
		}
	}

	public static void attachPolicyToGroup(IamClient iamClient, String groupName, String policyArn) {
		try {
			AttachGroupPolicyRequest attachPolicyRequest = AttachGroupPolicyRequest.builder().groupName(groupName)
					.policyArn(policyArn).build();

			iamClient.attachGroupPolicy(attachPolicyRequest);
			System.out.println("Successfully attached policy " + policyArn + " to group " + groupName);

		} catch (IamException e) {
			System.err
					.println("Failed to attach policy " + policyArn + " to group " + groupName + ": " + e.getMessage());
		}
	}
}
