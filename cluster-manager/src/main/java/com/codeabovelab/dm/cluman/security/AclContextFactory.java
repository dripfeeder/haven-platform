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

package com.codeabovelab.dm.cluman.security;

import com.codeabovelab.dm.common.security.acl.ExtPermissionGrantingStrategy;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 */
public class AclContextFactory {
    private static final ThreadLocal<AclContext> TL = new ThreadLocal<>();
    private static final Object lock = new Object();
    private static volatile AclContextFactory instance;

    final AclService aclService;
    final ExtPermissionGrantingStrategy pgs;
    final SidRetrievalStrategy sidStrategy;

    public AclContextFactory(AclService aclService, ExtPermissionGrantingStrategy pgs, SidRetrievalStrategy sidStrategy) {
        this.aclService = aclService;
        this.pgs = pgs;
        this.sidStrategy = sidStrategy;
    }

    /**
     * * Obtain context from thread local, if it actual and not null, otherwise create new context (but not place it to thread local).
     * @see #open()
     * @return not null context
     */
    public AclContext getContext() {
        AclContext ac = getLocalContext();
        if(ac == null) {
            ac = new AclContext(this);
        }
        return ac;
    }

    /**
     * Obtain context from thread local.
     * @return context or null
     */
    public static AclContext getLocalContext() {
        AclContext ac = TL.get();
        if(ac != null) {
            if(!ac.isActual()) {
                // auth is changed, we can not return default acl
                ac = null;
            }
        }
        return ac;
    }

    /**
     * Open context and place it to thread local.
     * @see #getContext()
     * @return
     */
    public AclContextHolder open() {
        AclContext old = TL.get();
        if(old != null && old.isActual()) {
            // context is not our, and is actual
            return new AclContextHolder(old, () -> {});
        }
        AclContext ac = new AclContext(this);
        TL.set(ac);
        return new AclContextHolder(ac, () -> {
            AclContext curr = TL.get();
            Assert.isTrue(ac == curr, "Invalid current context: " + curr + " expect: " + ac);
            TL.set(old);
        });
    }

    /**
     * It must not be public
     * @return current factory
     */
    static AclContextFactory getInstance() {
        synchronized (lock) {
            if(instance == null) {
                throw new IllegalStateException("No instance.");
            }
            return instance;
        }
    }

    @PostConstruct
    public void postConstruct() {
        synchronized (lock) {
            if(instance != null) {
                throw new IllegalStateException("Factory already has instance.");
            }
            instance = this;
        }
    }

    @PreDestroy
    public void preDestroy() {
        synchronized (lock) {
            if(instance != this) {
                throw new IllegalStateException("Factory has different instance.");
            }
            instance = null;
        }
    }
}
