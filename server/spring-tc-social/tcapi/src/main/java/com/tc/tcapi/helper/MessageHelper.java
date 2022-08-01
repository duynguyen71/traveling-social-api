package com.tc.tcapi.helper;

import com.tc.core.model.ChatGroup;
import com.tc.core.model.Message;
import com.tc.core.model.User;
import com.tc.core.request.BaseParamRequest;
import com.tc.core.request.MessageRequest;
import com.tc.core.response.BaseResponse;
import com.tc.core.response.MessageResponse;
import com.tc.tcapi.service.ChatGroupService;
import com.tc.tcapi.service.ChatGroupUserService;
import com.tc.tcapi.service.MessageService;
import com.tc.tcapi.service.UserService;
import com.tc.core.utilities.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class MessageHelper {

    private final MessageService messageService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ChatGroupUserService chatGroupUserService;
    private final ChatGroupService chatGroupService;

    public ResponseEntity<?> getMessages(Long groupId, Map<String, String> param) {
        User currentUser = userService.getCurrentUser();
        ChatGroup chatGroup = chatGroupService.getGroup(groupId, currentUser, 1);
        if (chatGroup == null) {
            return BaseResponse.badRequest("can not find chat group with id : " + groupId);
        }
        BaseParamRequest baseParamRequest = new BaseParamRequest(param);
        baseParamRequest.setSortBy("createDate");
        Pageable pageable = baseParamRequest.toPageRequest();
        List<Message> messages = messageService.getMessages(chatGroup, 1, pageable);
        List<MessageResponse> messageResponses = messages.stream()
                .map(m -> modelMapper.map(m, MessageResponse.class))
                .collect(Collectors.toList());

        return BaseResponse.success(messageResponses, "get message in group " + groupId + " success");
    }

    public ResponseEntity<?> saveMessage(Long chatGroupId, MessageRequest messageRequest) {
        String messageText = messageRequest.getMessage();
        List<Long> attachments = messageRequest.getAttachments();
        // check message text & attachment both null or not
        if (ValidationUtil.isNullOrBlank(messageText) && attachments.isEmpty()) {
            return BaseResponse.badRequest("message is not valid");
        }
        User currentUser = userService.getCurrentUser();
        ChatGroup chatGroup = chatGroupService.getGroup(chatGroupId, currentUser, 1);
        if (chatGroup == null) {

            return BaseResponse.badRequest("can not find chat group with id : " + chatGroupId);
        }
        Message message = new Message();
        message.setMessage(messageText);
        message.setUser(currentUser);
        message.setStatus(1);
        message.setChatGroup(chatGroup);
        //get reply message
        Long replyMessageId = messageRequest.getReplyMessageId();
        Message replyMessage = null;
        if ((replyMessage = messageService.getMessageInGroup(replyMessageId, 1, chatGroup)) != null) {
            message.setReplyMessage(replyMessage);
        }
        //save
        message = messageService.saveFlush(message);
        MessageResponse messageResponse = modelMapper.map(message, MessageResponse.class);
        return BaseResponse.success(messageResponse, "save message success");
    }


    public ResponseEntity<?> removeMessage(Long messageId) {
        Message message = messageService.getMessage(messageId, 1, userService.getCurrentUser());
        if (message == null) {
            return BaseResponse.badRequest("message is not exist");
        }
        message.setStatus(0);
        messageService.save(message);
        return BaseResponse.success("remove message success");
    }
}
