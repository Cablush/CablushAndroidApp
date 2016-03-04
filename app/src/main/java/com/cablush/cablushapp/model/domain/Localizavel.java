package com.cablush.cablushapp.model.domain;

import java.io.Serializable;

/**
 * Created by oscar on 13/12/15.
 */
public interface Localizavel extends Serializable {

    String getUuid();

    String getNome();

    String getDescricao();

    Local getLocal();

    String getWebsite();

    String getFacebook();

    String getImagemURL();

    String getResponsavel();

    Boolean isRemote();

    Boolean isChanged();
}
