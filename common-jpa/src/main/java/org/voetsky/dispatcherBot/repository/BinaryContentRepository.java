package org.voetsky.dispatcherBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.voetsky.dispatcherBot.entity.BinaryContent;

public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {

}
