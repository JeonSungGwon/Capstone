package com.example.Capstone.api;

import com.example.Capstone.dto.GroupDto;
import com.example.Capstone.dto.GroupMessageDto;
import com.example.Capstone.dto.MemberResponseDto;
import com.example.Capstone.entity.*;
import com.example.Capstone.repository.GroupMessageRepository;
import com.example.Capstone.repository.GroupRepository;
import com.example.Capstone.repository.MemberRepository;
import com.example.Capstone.service.GroupMessageService;
import com.example.Capstone.service.GroupService;
import com.example.Capstone.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "그룹 메시지")
public class GroupMessageController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final GroupMessageService groupMessageService;
    private final GroupService groupService;
    private final GroupMessageRepository groupMessageRepository;

    public GroupMessageController(MemberService memberService, MemberRepository memberRepository, GroupMessageService groupMessageService,
                                  GroupMessageRepository groupMessageRepository, GroupService groupService) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
        this.groupMessageService = groupMessageService;
        this.groupMessageRepository = groupMessageRepository;
        this.groupService = groupService;
    }

    @PostMapping("/group/accept") //그룹 참여 요청 메시지
    @Operation(summary = "그룹 참여 요청 메시지 전송")
    public ResponseEntity<String> acceptGroupRequest(@RequestParam String sharedCode) {
        return groupMessageService.acceptGroupRequest(sharedCode);
    }
    @GetMapping("/group/messages")
    @Operation(summary = "모든 그룹 메시지 불러오기")
    public List<GroupMessageDto> getGroupMessages() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        Member member = memberRepository.findById(myInfoBySecurity.getId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));

        List<GroupMessage> messages = groupMessageRepository.findByOwner(member);
        return messages.stream()
                .map(message -> new GroupMessageDto(message.getId(), message.getMessage(), message.getGroup().getId(), message.getGroup().getName(), message.getSender().getEmail(), message.getSender().getNickname(), message.getOwner().getId(), message.getGroup().getSharedCode()))
                .collect(Collectors.toList());
    }

    @GetMapping("/accept/message/{id}")  // 메시지로 온 그룹 요청 승인
    @Operation(summary = "메시지로 온 그룹 요청 승인")
    public GroupDto acceptGroupRequest(@PathVariable Long id ,@RequestParam String sharedCode, @RequestParam String email) {
        // 그룹 멤버를 추가하고 승인하는 로직 수행
        GroupDto groupDto = groupService.addMemberToGroup(sharedCode, email);

        GroupMessage groupMessage = groupMessageRepository.findById(id).orElse(null);
        groupMessageRepository.delete(groupMessage);
        // 그룹 멤버 추가 후의 결과 반환
        return groupDto;
    }

    @DeleteMapping("/reject/message/{id}")
    @Operation(summary = "메시지로 온 그룹 요청 거부")
    public ResponseEntity<String> disApproveGroupRequest(@PathVariable Long id){
        GroupMessage groupMessage = groupMessageRepository.findById(id).orElse(null);
        groupMessageRepository.delete(groupMessage);
        return ResponseEntity.ok("Shared Schedule disapproved successfully");
    }


}

