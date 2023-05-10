package com.example.Capstone.api;

import com.example.Capstone.dto.MemberResponseDto;
import com.example.Capstone.dto.MessageDto;
import com.example.Capstone.entity.Member;
import com.example.Capstone.repository.MemberRepository;
import com.example.Capstone.service.MemberService;
import com.example.Capstone.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@Api(tags = "Messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final MemberRepository memberRepository;

    private final MemberService memberService;

    @PostMapping
    @ApiOperation(value = "Write a message")
    public ResponseEntity<MessageDto> write(@RequestBody MessageDto messageDto) {
        MessageDto savedMessage = messageService.write(messageDto);
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/received")
    @ApiOperation(value = "Get received messages")
    public ResponseEntity<List<MessageDto>> receivedMessage() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        Member received = memberRepository.findByNickname(myInfoBySecurity.getNickname());
//        Member receiver = memberRepository.findByNickname(memberNickname); // 이 부분 고쳐야됨
        List<MessageDto> messages = messageService.receivedMessage(received);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/received/{id}")
    @ApiOperation(value = "Delete a received message")
    public ResponseEntity<Object> deleteReceivedMessage(@PathVariable int id, @RequestParam String memberNickname) {
        Member receiver = memberRepository.findByNickname(memberNickname);
        Object result = messageService.deleteMessageByReceiver(id, receiver);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/sent")
    @ApiOperation(value = "Get sent messages")
    public ResponseEntity<List<MessageDto>> sentMessage(@RequestParam String memberNickname) {
        Member sender = memberRepository.findByNickname(memberNickname);
        List<MessageDto> messages = messageService.sentMessage(sender);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/sent/{id}")
    @ApiOperation(value = "Delete a sent message")
    public ResponseEntity<Object> deleteSentMessage(@PathVariable int id, @RequestParam String memberNickname) {
        Member sender = memberRepository.findByNickname(memberNickname); //
        Object result = messageService.deleteMessageBySender(id, sender);
        return ResponseEntity.ok(result);
    }
}

