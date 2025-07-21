package kr.spring.client.dao;

import kr.spring.client.vo.ClientVO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClientMapper {
    List<ClientVO> selectClientList();
    ClientVO selectClient(Long client_num);
    void insertClient(ClientVO clientVO);
    void updateClient(ClientVO clientVO);
    void deleteClient(Long client_num);
    List<Map<String, Object>> selectClientSalesStats();
} 