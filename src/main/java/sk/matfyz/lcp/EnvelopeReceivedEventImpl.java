/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.matfyz.lcp;

import sk.matfyz.lcp.api.Envelope;
import sk.matfyz.lcp.api.EnvelopeReceivedEvent;


public class EnvelopeReceivedEventImpl implements EnvelopeReceivedEvent {
    
    private final Envelope envelope;
    
    public EnvelopeReceivedEventImpl(Envelope envelope) {
        this.envelope = envelope;
    }

    @Override
    public Envelope getEnvelope() {
        return envelope;
    }
    
}
