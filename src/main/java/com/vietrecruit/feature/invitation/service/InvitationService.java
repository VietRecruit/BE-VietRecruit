package com.vietrecruit.feature.invitation.service;

import com.vietrecruit.feature.invitation.dto.CreateInvitationRequest;
import com.vietrecruit.feature.invitation.dto.InvitationResponse;

public interface InvitationService {

    /**
     * Creates a new invitation, assigns the target role, and sends the invitation email.
     *
     * @param request invitation details including recipient email and role assignment
     * @return the created invitation response including the invitation token
     */
    InvitationResponse create(CreateInvitationRequest request);
}
