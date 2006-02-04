package org.apache.myfaces.tobago.util;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Dec 6, 2005
 * Time: 7:50:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugPhaseListener implements PhaseListener {
  private static final Log LOG = LogFactory.getLog(DebugPhaseListener.class);
  public void afterPhase(PhaseEvent phaseEvent) {
    LOG.debug("After Phase :" + phaseEvent.getPhaseId());
  }

  public void beforePhase(PhaseEvent phaseEvent) {
    LOG.debug("Before Phase :" + phaseEvent.getPhaseId());
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
