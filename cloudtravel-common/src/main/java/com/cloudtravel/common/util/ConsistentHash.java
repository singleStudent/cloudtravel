package com.cloudtravel.common.util;

import com.cloudtravel.common.service.IHashService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Administrator on 2021/7/18 0018.
 */
@Slf4j
public class ConsistentHash<T> {

    private final IHashService iHashService;

    /** 每个机器节点关联的虚拟节点数量 */
    private final int numberOfReplicas;

    /** 环形虚拟节点对应的映射 */
    private final SortedMap<Long , T> circle = new TreeMap<Long, T>();

    public ConsistentHash(IHashService iHashService, int numberOfReplicas , Collection<T> nodes) {
        log.info("环形节点服务初始化,numberOfReplicas={}" , numberOfReplicas);
        this.iHashService = iHashService;
        this.numberOfReplicas = numberOfReplicas;
        for (T node : nodes) {
            addRealNodes(node);
        }
    }

    /**
     * 增加真实机器节点, 并增加真实节点的虚拟映射节点数量为numberOfReplicas
     * @param node
     */
    public void addRealNodes(T node) {
        for(int i = 0; i < this.numberOfReplicas; i++) {
            circle.put(this.iHashService.hash(node.toString() + i) , node);
        }
    }

    /**
     * 移除真实节点 , 并将该真实节点对应的虚拟节点去除,去除节点为numberOfReplicas
     * @param node
     */
    public void removeRealNode(T node) {
        for(int i = 0 ; i < numberOfReplicas; i ++) {
            circle.remove(this.iHashService.hash(node.toString() + i));
        }
    }

    /**
     * 根据key获取对应的节点位
     * @param key
     * @return
     */
    public T get(String key) {
        if(circle.isEmpty()) {
            return null;
        }
        long hash = iHashService.hash(key);
        //沿着节点圆环找到一个虚拟节点
        if(!circle.containsKey(hash)) {
            SortedMap<Long , T> tailMap = circle.tailMap(hash);
            hash = tailMap.isEmpty() ? circle.firstKey() : tailMap.firstKey();
        }
        return circle.get(hash);
    }
}
