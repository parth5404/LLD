package Repo;

import models.Branch;

import java.util.HashMap;
import java.util.Map;

public class BranchRepo {
    private final Map<String, Branch> branches=new HashMap<>();

    public void addBranch(String id, Branch br){
        branches.put(id,br);
    }
    public Branch getBranchById(String id){
        return branches.get(id);
    }
}
