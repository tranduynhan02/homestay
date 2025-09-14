package com.nhantd.homestay.service;

import com.nhantd.homestay.dto.request.BranchRequest;
import com.nhantd.homestay.dto.response.BranchResponse;
import com.nhantd.homestay.model.Branch;
import com.nhantd.homestay.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;

    public BranchResponse createBranch(BranchRequest request) {
        Branch branch = new Branch();
        branch.setBranchName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());
        return toResponse(branchRepository.save(branch));
    }

    public List<BranchResponse> getAllBranches() {
        return branchRepository.findAll().stream().map(this::toResponse).toList();
    }

    public BranchResponse getBranchById(Long id) {
        return branchRepository.findById(id).map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }

    public BranchResponse updateBranch(Long id, BranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        branch.setBranchName(request.getName());
        branch.setAddress(request.getAddress());
        branch.setPhone(request.getPhone());
        return toResponse(branchRepository.save(branch));
    }

    private BranchResponse toResponse(Branch branch) {
        BranchResponse dto = new BranchResponse();
        dto.setId(branch.getId());
        dto.setName(branch.getBranchName());
        dto.setAddress(branch.getAddress());
        dto.setPhone(branch.getPhone());
        return dto;
    }
}
