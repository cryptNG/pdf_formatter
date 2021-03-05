package pdf.formatter.pdf_formatter.repositories;

import org.springframework.stereotype.Repository;

//import org.springframework.data.jpa.repository.JpaRepository;

import pdf.formatter.pdf_formatter.entities.Order;

@Repository
public interface OrderRepository extends BaseRepository<Order, Long> {

    public Order findByRequestId(String requestId);

}
