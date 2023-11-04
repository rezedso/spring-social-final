package com.example.demo.controller;

import com.example.demo.dto.response.CommunityResponseDto;
import com.example.demo.dto.response.PageDto;
import com.example.demo.dto.response.PostResponseDto;
import com.example.demo.service.ISearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MOD') or hasRole('ROLE_ADMIN')")
public class SearchController {
    private final ISearchService searchService;

    @GetMapping("/page/{page}")
    public ResponseEntity<PageDto<PostResponseDto>> getPostsByTitleOrAuthorContaining(
            @RequestParam("query") String query,
            @PathVariable("page") int page
    ) {
        return ResponseEntity.ok(searchService.getPostsByTitleOrAuthor(query, page));
    }
}
