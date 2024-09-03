package com.example;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.DeleteGroupRequest;
import software.amazon.awssdk.services.iam.model.ListGroupsRequest;
import software.amazon.awssdk.services.iam.model.ListGroupsResponse;
import software.amazon.awssdk.services.iam.model.ListAttachedGroupPoliciesRequest;
import software.amazon.awssdk.services.iam.model.ListAttachedGroupPoliciesResponse;
import software.amazon.awssdk.services.iam.model.DetachGroupPolicyRequest;
import software.amazon.awssdk.services.iam.model.Group;
import software.amazon.awssdk.services.iam.model.IamException;
import software.amazon.awssdk.services.iam.model.AttachedPolicy;

public class DeleteIAMGroups {

    public static void main(String[] args) {

        Region region = Region.AWS_GLOBAL;
        IamClient iamClient = IamClient.builder()
                .region(region)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        try {
            deleteAllGroups(iamClient);
        } catch (IamException e) {
            System.err.println("Failed to delete groups: " + e.getMessage());
        } finally {
            iamClient.close();
        }
    }

    public static void deleteAllGroups(IamClient iamClient) {
        try {
            ListGroupsRequest listGroupsRequest = ListGroupsRequest.builder().build();
            ListGroupsResponse listGroupsResponse = iamClient.listGroups(listGroupsRequest);

            for (Group group : listGroupsResponse.groups()) {
                String groupName = group.groupName();
                detachAllPoliciesFromGroup(iamClient, groupName);
                deleteGroup(iamClient, groupName);
            }
        } catch (IamException e) {
            System.err.println("Failed to list groups: " + e.getMessage());
        }
    }

    public static void detachAllPoliciesFromGroup(IamClient iamClient, String groupName) {
        try {
            ListAttachedGroupPoliciesRequest listAttachedGroupPoliciesRequest = ListAttachedGroupPoliciesRequest.builder()
                    .groupName(groupName)
                    .build();
            ListAttachedGroupPoliciesResponse listAttachedGroupPoliciesResponse = iamClient.listAttachedGroupPolicies(listAttachedGroupPoliciesRequest);

            for (AttachedPolicy policy : listAttachedGroupPoliciesResponse.attachedPolicies()) {
                String policyArn = policy.policyArn();
                detachPolicyFromGroup(iamClient, groupName, policyArn);
            }
        } catch (IamException e) {
            System.err.println("Failed to list attached policies for group " + groupName + ": " + e.getMessage());
        }
    }

    public static void detachPolicyFromGroup(IamClient iamClient, String groupName, String policyArn) {
        try {
            DetachGroupPolicyRequest detachPolicyRequest = DetachGroupPolicyRequest.builder()
                    .groupName(groupName)
                    .policyArn(policyArn)
                    .build();
            iamClient.detachGroupPolicy(detachPolicyRequest);
            System.out.println("Successfully detached policy " + policyArn + " from group " + groupName);
        } catch (IamException e) {
            System.err.println("Failed to detach policy " + policyArn + " from group " + groupName + ": " + e.getMessage());
        }
    }

    public static void deleteGroup(IamClient iamClient, String groupName) {
        try {
            DeleteGroupRequest deleteGroupRequest = DeleteGroupRequest.builder()
                    .groupName(groupName)
                    .build();
            iamClient.deleteGroup(deleteGroupRequest);
            System.out.println("Successfully deleted group: " + groupName);
        } catch (IamException e) {
            System.err.println("Failed to delete group " + groupName + ": " + e.getMessage());
        }
    }
}
