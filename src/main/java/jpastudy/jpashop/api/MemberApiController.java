package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Member;
import jpastudy.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member)
    {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    // -- 응답과 용청에 사용할 Inner Class -- //
    @Data
    static class CreateMemberResponse
    {
        private final Long id;
        public CreateMemberResponse(Long id)
        {
            this.id = id;
        }
    }
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }
    @Data
    static class CreateMemberRequest
    {
        @NotEmpty
        private String name;
    }
    // 수정 API
    @PatchMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, @RequestBody @Valid UpdateMemberRequest request)
    {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }
    @Data
    static class UpdateMemberRequest
    {
        private String name;
    }
    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse
    {
        private Long id;
        private String name;
    }
    /**
     * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }
    /**
     * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다 (Result를 List로 바꿈)
     */
    @GetMapping("/api/v2/members")
    public List<MemberDto> membersV2() {
        List<Member> findMembers = memberService.findMembers();
        //엔티티 -> DTO 변환
        List<MemberDto> memberDtoList = findMembers.stream() // Stream<Member>
                .map(m -> new MemberDto(m.getName())) // Stream<MemberDto>
                .collect(toList()); // List<MemberDto>
        return memberDtoList;
    }
    /**
     * 조회 V2.1: Result<List<MemberDto>>로 하겠다 (ppt에 있는 부분)
     */
    @GetMapping("/api/v2_1/members")
    public Result membersV2_1() {
        List<Member> findMembers = memberService.findMembers();
        //엔티티 -> DTO 변환
        List<MemberDto> memberDtoList = findMembers.stream() // Stream<Member>
                .map(m -> new MemberDto(m.getName())) // Stream<MemberDto>
                .collect(toList()); // List<MemberDto>
        return new Result(memberDtoList.size(), memberDtoList);
    }
    @Data
    @AllArgsConstructor
    class Result<T> {
        private int count;
        private T data;
    }
    @Data
    @AllArgsConstructor
    class MemberDto {
        private String name;
    }
}
