package com.bosnjak.serverproject2;

import java.sql.Date;
import java.sql.Timestamp;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * RESTful Web Service - Timecards Tracker
 *
 * @author Dominik Bosnjak and Josipa Puljko
 */
@Path("CompanyService")
public class CompanyService {

    BusinessLayer bl = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompanyService
     */
    public CompanyService() {
        this.bl = new BusinessLayer();
    }

    @GET
    @Path("departments")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDepartments(@QueryParam("company") String company) {
        return bl.getDepartments(company);
    }

    /**
     * TODO: Implement the method to get the record for a specific department.
     *
     * @param company
     * @param deptId
     * @return
     */
    @Path("department")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDepartment(@QueryParam("company") String company, @QueryParam("dept_id") int deptId) {
        return bl.getDepartment(company, deptId);
    }

    /**
     * TODO: Implement the method to update a department.
     *
     * @param department
     * @return
     */
    @Path("department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateDepartment(String department) {
        return bl.updateDepatment(department);
    }

    /**
     * TODO: Implement the method to create a new department.
     *
     * @param company
     * @param deptName
     * @param deptNo
     * @param location
     * @return
     */
    @Path("department")
    @POST //Used to delete an existing resource
    @Produces(MediaType.APPLICATION_JSON)
    public String insertDepartment(@FormParam("company") String company, @FormParam("dept_name") String deptName, @FormParam("dept_no") String deptNo, @FormParam("location") String location) {
        return bl.insertDepartment(company, deptName, deptNo, location);
    }

    /**
     * TODO: Implement the method to delete a department
     *
     * @param company
     * @param deptId
     * @return
     */
    @Path("department")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDepartment(@QueryParam("company") String company, @QueryParam("dept_id") int deptId) {
        return bl.deleteDepartment(company, deptId);
    }

    /**
     * Employee methods
     */
    
    /**
     * TODO: Implement method to get all employees
     *
     * @param company
     * @return
     */
    @GET
    @Path("employees")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmployees(@QueryParam("company") String company) {
        return bl.getEmployees(company);
    }

    /**
     * TODO: Implement the method to get the record for a specific employee.
     *
     * @param emp_id
     * @return
     */
    @Path("employee")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmployee(@QueryParam("emp_id") int empId) {
        return bl.getEmployee(empId);
    }

    /**
     * TODO: Implement the method to create a new employee.
     *
     * @param company
     * @param deptName
     * @param deptNo
     * @param location
     * @return
     */
    @Path("employee")
    @POST //Used to insert new resource
    @Produces(MediaType.APPLICATION_JSON)
    public String insertEmployee(@FormParam("emp_name") String empName, @FormParam("emp_no") String empNo, @FormParam("hire_date") String hireDate, @FormParam("job") String job, @FormParam("salary") Double salary, @FormParam("dept_id") int deptId, @FormParam("mng_id") int mngId) {
        return bl.insertEmployee(empName, empNo, hireDate, job, salary, deptId, mngId);
    }

    /**
     * TODO: Implement the method to update a employee.
     *
     * @param employee
     * @return
     */
    @Path("employee")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(String employee) {
        return bl.updateEmployee(employee);
    }

    /**
     * TODO: Implement the method to delete a department
     *
     * @param empId
     * @return
     */
    @Path("employee")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteEmployee(@QueryParam("emp_id") int empId) {
        return bl.deleteEmployee(empId);
    }

    /*
    *Timecards methods
     */
    
    /**
     * TODO: Implement method to get all timecards
     *
     * @param emp_id
     * @return
     */
    @GET
    @Path("timecards")
    @Produces(MediaType.APPLICATION_JSON)
    public String getTimecardss(@QueryParam("emp_id") int empId) {
        return bl.getTimecards(empId);
    }

    /**
     * TODO: Implement the method to get the record for a specific timecard.
     *
     * @param timecard
     * @return
     */
    @Path("timecard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTimecard(@QueryParam("timecard") int timecard) {
        return bl.getTimecard(timecard);
    }

    /**
     * TODO: Implement the method to create a new timecard.
     *
     * @param empId
     * @param startTime
     * @param endTime
     * @return
     */
    @Path("timecard")
    @POST //Used to delete an existing resource
    @Produces(MediaType.APPLICATION_JSON)
    public String insertTimecard(@FormParam("emp_id") int empId, @FormParam("start_time") Timestamp startTime, @FormParam("end_time") Timestamp endTime) {
        return bl.insertTimecard(empId, startTime, endTime);
    }

    /**
     * TODO: Implement the method to update a Timecard.
     *
     * @param Timecard
     * @return
     */
    @Path("timecard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTimecard(String timecard) {
        return bl.updateTimecard(timecard);
    }

    /**
     * TODO: Implement the method to delete a timecard
     *
     * @param timecard
     * @return
     */
    @Path("timecard")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTimecard(@QueryParam("timecard_id") int timecardId) {
        return bl.deleteTimecard(timecardId);
    }

}
