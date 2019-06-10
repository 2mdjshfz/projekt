/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package books;

/**
 *
 * @author Admin
 */
public class ServerResponse {
    public int Status;
    public Object ResponseObject;
    
    public ServerResponse(int status, Object responseObject)
    {
        Status = status;
        ResponseObject = responseObject;
    }
}
