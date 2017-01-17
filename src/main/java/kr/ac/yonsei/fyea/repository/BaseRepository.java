//package kr.ac.yonsei.fyea.repository;
//
//import org.springframework.data.repository.NoRepositoryBean;
//import org.springframework.data.repository.Repository;
//
//import java.io.Serializable;
//import java.util.Optional;
//
//@NoRepositoryBean
//public interface BaseRepository<T, ID extends Serializable> extends Repository<T, ID> {
//
//    <S extends T> S save(S entity);
//
//    Optional<T> findOne(ID id);
//
//    Iterable<T> findAll();
//}
