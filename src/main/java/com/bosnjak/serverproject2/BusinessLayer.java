package com.bosnjak.serverproject2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import companydata.DataLayer;
import companydata.Department;
import companydata.Employee;
import companydata.Timecard;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 * Represents the Business Layer for the Web Service
 *
 * @author Dominik Bosnjak and Josipa Puljko
 */
class BusinessLayer {

    private DataLayer dl = null;
    Gson gson = null;
    Gson gsonCard = null;

    public BusinessLayer() {
        try {
            this.dl = new DataLayer("development");
            //this.gson = new Gson();
            this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            this.gsonCard = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

            //this.gson = new Gson.format()
        } catch (Exception ex) {
            Logger.getLogger(BusinessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * GET_DEPARTMENTS
     *
     * @param company
     * @return
     */
    public String getDepartments(String company) {
        List<Department> departments = dl.getAllDepartment(company);
        if (departments.isEmpty()) {
            return "{\"error\":\"No records found. Either the comapny name is wrong or there are no departments for that company.\"}";
        }
        
        return gson.toJson(departments);
    }

    
    /**
     * GET_DEPARTMENT
     *
     * @param company
     * @param deptId
     * @return
     */
    public String getDepartment(String company, int deptId) {

        Department dept = dl.getDepartment(company, deptId);
        
        //checks if the department ID exists in database
        if (dept == null) {
            return "{\"error\":\"Depatment doesn't exist, please enter the existing department ID.\"}";
        }
        return gson.toJson(dept);
    }

    
    /**
     * UPDATE_DEPARTMENT
     *
     * @param department
     * @return
     */
    public String updateDepatment(String department) {
        Department dept = gson.fromJson(department, Department.class);
        
        Department checkDbForDept = dl.getDepartment(dept.getCompany(), dept.getId());
        
        List<Department> listOfDepartments = dl.getAllDepartment(dept.getCompany());
        
        if (dept.getId() == 0) {

            return "{\"error\":\"Please provide department ID.\"}";
            
        }

        //checks if the company name exists as a parameter
        if (dept.getCompany() == null) {
            return "{\"error\":\"Please provide company name.\"}";
        }
        
        //checks if the department exists
        if (checkDbForDept == null) {
            return "{\"error\":\"This Department ID doesn't exist.\"}";
        }

        //checks if department name is provided for change
        if (dept.getDeptName() != null) {

            checkDbForDept.setDeptName(dept.getDeptName());
        }
        
        //checks if department number is provided for change
        if (dept.getDeptNo() != null) {
            

            //checks if there is an existing department number in the previous department list
            for (int i = 0; i < listOfDepartments.size(); i++) {
                if (dept.getDeptNo().equals(listOfDepartments.get(i).getDeptNo())) {
                    
                    return "{\"error\":\"The department number already exists, please provide different number for the new department number.\"}";
                
                } else {
                
                    checkDbForDept.setDeptNo(dept.getDeptNo());

                }
            }
        }

        //checks if department location is provided for change
        if (dept.getLocation() != null) {

            checkDbForDept.setLocation(dept.getLocation());
        }

        return gson.toJson(dl.updateDepartment(checkDbForDept));

    }

    
    /**
     * INSERT_DEPARTMENT
     *
     * @param company
     * @param deptName
     * @param deptNo
     * @param location
     * @return
     */
    public String insertDepartment(String company, String deptName, String deptNo, String location) {

        Department department = new Department(company, deptName, deptNo, location);

        List<Department> listOfDepartments = dl.getAllDepartment(company);

        //checks if there is an existing department number in the previous department list
        for (int i = 0; i < listOfDepartments.size(); i++) {
            if (department.getDeptNo().equals(listOfDepartments.get(i).getDeptNo())) {
                return "{\"error\":\"The department number already exists, please provide different number for the new department number.\"}";
            }
        }

        Department dept2 = dl.insertDepartment(department);

        return gson.toJson(dept2);
    }

    
    /**
     * DELETE_DEPARTMENT
     *
     * @param deptId
     * @param company
     * @return
     */
    public Response deleteDepartment(String company, int deptId) {        
 
        int department_id = dl.deleteDepartment(company, deptId);

        return Response.status(Response.Status.OK).build();
    }
    
    
    /**
     * EMPLOYEES
     */
    
    /**
     * GET_EMPLOYEES
     *
     * @param company
     * @return
     */
    public String getEmployees(String company) {
        List<Employee> employees = dl.getAllEmployee(company);
        
        //checks if the list employee has any data, or is empty
        if (employees.isEmpty()) {
            return "{\"error\":\"No employee records found. Company name doesn't exist.\"}";
        }
        return gson.toJson(employees);
    }

    
    /**
     * GET_EMPLOYEE
     *
     * @param empId
     * @return
     */
    public String getEmployee(int empId) {
        Employee employee = dl.getEmployee(empId);
        
        //checks if the new object has been created or the resutl is null which means ID doesn't exist
        if (employee == null) {
            return "{\"error\":\"Employee doesn't exist, please provide the correct employee ID.\"}";
        }
        
        return gson.toJson(employee);
    }

    
    /**
     * GET_EMPLOYEE
     *
     * @param empName
     * @param empNo
     * @param hireDate
     * @param job
     * @param salary
     * @param deptId
     * @param mngId
     * @return
     */
    public String insertEmployee(String empName, String empNo, String hireDate, String job, Double salary, int deptId, int mngId) {

        java.util.Date newHireDate = null;

        //dept_id must exist as a Department in your company
        if (dl.getDepartment("dxb3501", deptId) == null) {
            return "{\"error\":\"Department doesn't exist.\"}";
        }

        List<Employee> listOfEmployees = dl.getAllEmployee("dxb3501");

        //checks if the employee numbers already exists
        for (int i = 0; i < listOfEmployees.size(); i++) {
            if (empNo.equals(listOfEmployees.get(i).getEmpNo())) {
                return "{\"error\":\"The employee number exist, please provide different employee number for the employee.\"}";
            }
        }

        //mng_id must be the record id of an existing Employee in your company. Use 0 if the first employee or any other employee that doesn’t have a manager.
        Employee checkManager = dl.getEmployee(mngId);

        if (checkManager == null && dl.getAllEmployee("dxb3501").size() == 0) {
            mngId = 0;
        } else if (checkManager == null) {
            return "{\"error\":\"Please enter valid manager ID\"}";
        }

        //hire_date must be a valid date equal to the current date or earlier (e.g. current date or in the past)
        try {
            newHireDate = new SimpleDateFormat("yyyy-MM-dd").parse(hireDate);
        } catch (ParseException e) {
            return "{\"error\":\"Must insert a valid date.\"}";
        }

        if (newHireDate.after(new Date(System.currentTimeMillis()))) {
            return "{\"error\":\"Date has to be previous to current date, or at least the current date.\"}";
        }

        Employee employee = new Employee(empName, empNo, new java.sql.Date(newHireDate.getTime()), job, salary, deptId, mngId);

        Employee employee2 = dl.insertEmployee(employee);

        return gson.toJson(employee2);
    }

    
    /**
     * UPDATE_EMPLOYEE
     *
     * @param employee
     * @return
     */
    public String updateEmployee(String employee) {

        Employee employee1 = new Employee();

        employee1 = gson.fromJson(employee, Employee.class);

        List<Employee> listOfEmployees = dl.getAllEmployee("dxb3501");

        List<Department> listOfDepartments = dl.getAllDepartment("dxb3501");

        //checks if the department ID exists
        for (int i = 0; i < listOfDepartments.size(); i++) {
            if (employee1.getDeptId() != listOfDepartments.get(i).getId()) {
                return "{\"error\":\"The department ID doesn't exist, please provide correct ID number for the department.\"}";
            }
        }

        //checks if the employee ID exists
        for (int i = 0; i < listOfEmployees.size(); i++) {
            if (employee1.getId() != listOfEmployees.get(i).getId()) {
                return "{\"error\":\"The employee ID doesn't exist, please provide correct ID number for the employee.\"}";
            }
        }

        Employee employee2 = dl.updateEmployee(employee1);

        return gson.toJson(employee2);
    }

    
    /**
     * DELETE_EMPLOYEE
     *
     * @param empId
     * @return
     */
    public Response deleteEmployee(int empId) {

        int employee = dl.deleteEmployee(empId);

        return Response.status(Response.Status.OK).build();

    }

    
    /**
     * Timecard methods
     */
    /**
     * GET_TIMECARDS
     *
     * @param empId
     * @return
     */
    public String getTimecards(int empId) {
        List<Timecard> timecards = dl.getAllTimecard(empId);
        if (timecards.isEmpty()) {
            return "{\"error\":\"No records found. Either the comapny name is wrong or there are no departments for that company.\"}";
        }
        return gsonCard.toJson(timecards);
    }

    
    /**
     * GET_TIMECARD
     *
     * @param timecard
     * @return
     */
    public String getTimecard(int timecard) {
        Timecard timecard1 = dl.getTimecard(timecard);

        return gsonCard.toJson(timecard1);

    }

    
    /**
     * INSERT_TIMECARD
     *
     * @param empId
     * @param startTime
     * @param endTime
     * @return
     */
    public String insertTimecard(int empId, Timestamp startTime, Timestamp endTime) {
        Timecard timecard1 = new Timecard(startTime, endTime, empId);

        Timecard timecard2 = dl.insertTimecard(timecard1);

        return gsonCard.toJson(timecard2);
    }

    
    /**
     * UPDATE_TIMECARD
     *
     * @param timecard
     * @return
     */
    public String updateTimecard(String timecard) {
        Timecard timecard1 = new Timecard();

        timecard1 = gson.fromJson(timecard, Timecard.class);

        Timecard timecard2 = dl.updateTimecard(timecard1);

        return gsonCard.toJson(timecard2);
    }

    
    /**
     * DELETE_TIMECARD
     *
     * @param timecard
     * @return
     */
    public Response deleteTimecard(int timecardId) {

        int timecard1 = dl.deleteTimecard(timecardId);
        return Response.status(Response.Status.OK).build();

    }

}
