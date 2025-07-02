package kr.spring.client.dao;

import kr.spring.client.vo.ClientVO;
import java.util.List;

public interface ClientMapper {
    List<ClientVO> selectClientList();
    ClientVO selectClient(Long client_num);
    void insertClient(ClientVO clientVO);
    void updateClient(ClientVO clientVO);
    void deleteClient(Long client_num);
} 