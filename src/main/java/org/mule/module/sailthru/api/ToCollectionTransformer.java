package org.mule.module.sailthru.api;

import java.util.Collection;

public interface ToCollectionTransformer
{
    <T> Collection<T> toCollection(Object input);
}
