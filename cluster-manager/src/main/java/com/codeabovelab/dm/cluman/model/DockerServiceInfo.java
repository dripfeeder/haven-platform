/*
 * Copyright 2016 Code Above Lab LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeabovelab.dm.cluman.model;

import com.google.common.collect.ImmutableList;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Information about Doker service and its nodes. <p/>
 * Be careful with fields, because they can be used in js ui.
 *
 */
@Data
public class DockerServiceInfo {

    private final String id;
    private final String name;
    private final Integer containers;
    private final Integer offContainers;
    private final Integer images;
    private final Integer ncpu;
    private final Double memory;
    private final Integer nodeCount;
    private final Integer offNodeCount;

    private final List<NodeInfo> nodeList;

    private DockerServiceInfo(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.containers = builder.containers;
        this.offContainers = builder.offContainers;
        this.images = builder.images;
        this.ncpu = builder.ncpu;
        this.memory = builder.memory;
        this.nodeCount = builder.nodeCount;
        this.offNodeCount = builder.offNodeCount;
        this.nodeList = ImmutableList.copyOf(builder.nodeList);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Data
    public static final class Builder {
        private String id;
        private String name;
        private Integer containers;
        private Integer offContainers;
        private Integer images;
        private Integer ncpu;
        private Double memory;
        private Integer nodeCount;
        private Integer offNodeCount;
        private final List<NodeInfo> nodeList = new ArrayList<>();

        private Builder() {
        }

        public DockerServiceInfo build() {
            return new DockerServiceInfo(this);
        }

        public Builder from(DockerServiceInfo o) {
            setId(o.getId());
            setName(o.getName());
            setContainers(o.getContainers());
            setOffContainers(o.getOffContainers());
            setImages(o.getImages());
            setNcpu(o.getNcpu());
            setMemory(o.getMemory());
            setNodeList(o.getNodeList());
            setNodeCount(o.getNodeCount());
            setOffNodeCount(o.getOffNodeCount());
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder containers(Integer containers) {
            this.containers = containers;
            return this;
        }

        public Builder offContainers(Integer offContainers) {
            setOffContainers(offContainers);
            return this;
        }

        public Builder images(Integer images) {
            this.images = images;
            return this;
        }

        public Builder ncpu(Integer ncpu) {
            this.ncpu = ncpu;
            return this;
        }

        public Builder memory(Double memory) {
            this.memory = memory;
            return this;
        }

        public Builder nodeCount(Integer nodeCount) {
            this.nodeCount = nodeCount;
            return this;
        }

        public Builder offNodeCount(Integer offNodeCount) {
            setOffNodeCount(offNodeCount);
            return this;
        }

        public Builder nodeList(List<? extends NodeInfo> nodeList) {
            setNodeList(nodeList);
            return this;
        }

        public void setNodeList(Collection<? extends NodeInfo> nodeList) {
            this.nodeList.clear();
            if(nodeList != null) {
                this.nodeList.addAll(nodeList);
            }
        }
    }
}
