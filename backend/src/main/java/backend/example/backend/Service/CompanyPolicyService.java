package backend.example.backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.example.backend.Entity.Company;
import backend.example.backend.Repository.CompanyRepository;

@Service
public class CompanyPolicyService {

    @Autowired
    private CompanyRepository companyPolicyRepository;

    //get policies of a specific employer's company
    public Company getPolicyByEmployer(String employerId) {
        return companyPolicyRepository.findByEmployerId(employerId);
    }

    //if policies need to be updated
    public Company updatePolicy(String employerId, Company updatedPolicy) {
        Company policy = companyPolicyRepository.findByEmployerId(employerId);
        policy.setMaxHoursPerWeek(updatedPolicy.getMaxHoursPerWeek());
        policy.setAllowNightShifts(updatedPolicy.isAllowNightShifts());
        policy.setWeekendOff(updatedPolicy.isWeekendOff());
        return companyPolicyRepository.save(policy);
    }
}
