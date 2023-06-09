package com.example.Capstone.api;

import com.example.Capstone.dto.GroupDto;
import com.example.Capstone.dto.MemberDto;
import com.example.Capstone.dto.MemberResponseDto;
import com.example.Capstone.dto.ScheduleDto;
import com.example.Capstone.entity.Member;
import com.example.Capstone.entity.MyGroup;
import com.example.Capstone.entity.Schedule;
import com.example.Capstone.entity.SharedSchedule;
import com.example.Capstone.repository.GroupRepository;
import com.example.Capstone.repository.MemberRepository;
import com.example.Capstone.service.GroupService;
import com.example.Capstone.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping("")
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto) {
        Long groupId = groupService.createGroup(groupDto);
        System.out.println(groupId);
        GroupDto savedGroupDto = groupService.getGroup(groupId);
        System.out.println(savedGroupDto.getId());
        return ResponseEntity.ok(savedGroupDto);
    }

    @DeleteMapping("owner/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.ok().build();
    }

    //@PostMapping("/code")
    //public ResponseEntity<GroupDto> addMemberToGroup(@RequestBody String sharedCode) {
    //    GroupDto savedGroupDto = groupService.addMemberToGroup(sharedCode);
    //    return ResponseEntity.ok(savedGroupDto);
    // }

    @DeleteMapping("/{groupId}/members/{memberId}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable Long groupId, @PathVariable Long memberId) {
        groupService.removeMemberFromGroup(groupId, memberId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Long groupId) {
        GroupDto groupDto;
        try {
            groupDto = groupService.getGroup(groupId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(groupDto);
    }

    @GetMapping("/mygroups")
    public List<GroupDto> getMyGroup() {
        MemberResponseDto myInfoBySecurity = memberService.getMyInfoBySecurity();
        Member member = memberRepository.findById(myInfoBySecurity.getId()).orElseThrow(() -> new EntityNotFoundException("Member not found"));

        List<MyGroup> ownedGroups = member.getOwnedGroups(); // Member 엔티티에서 현재 사용자가 소유한 그룹 리스트 가져오기
        List<MyGroup> memberGroups = member.getGroups(); // Member 엔티티에서 현재 사용자가 멤버로 참여한 그룹 리스트 가져오기

        List<GroupDto> groupDtos = new ArrayList<>();

        // 소유한 그룹을 GroupDto로 변환하여 추가
        for (MyGroup group : ownedGroups) {
            GroupDto groupDto = new GroupDto(group.getId(), group.getName(), group.getOwner().getId(), group.getMembers(), group.getSharedCode());
            groupDtos.add(groupDto);
        }

        // 멤버로 참여한 그룹을 GroupDto로 변환하여 추가
        for (MyGroup group : memberGroups) {
            // 이미 추가된 그룹은 중복으로 추가하지 않도록 확인
            if (!ownedGroups.contains(group)) {
                GroupDto groupDto = new GroupDto(group.getId(), group.getName(), group.getOwner().getId(), group.getMembers(), group.getSharedCode());
                groupDtos.add(groupDto);
            }
        }

        return groupDtos;
    }



}