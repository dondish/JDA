/*
 *     Copyright 2015-2017 Austin Keener & Michael Ritter & Florian Spieß
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

package net.dv8tion.jda.core.requests;

import net.dv8tion.jda.core.entities.impl.JDAImpl;
import okhttp3.RequestBody;
import org.apache.commons.collections4.map.CaseInsensitiveMap;

import java.util.concurrent.CompletableFuture;

public class RestFuture<T> extends CompletableFuture<T> implements RequestFuture<T>
{
    final Request<T> request;

    public RestFuture(final RestAction<T> restAction, final boolean shouldQueue, final RequestBody data, final Object rawData, final Route.CompiledRoute route, final CaseInsensitiveMap<String, String> headers)
    {
        this.request = new Request<>(restAction, this::complete, this::completeExceptionally, shouldQueue, data, rawData, route, headers);
        ((JDAImpl) restAction.getJDA()).getRequester().request(this.request);
    }

    public RestFuture(final T t)
    {
        this.request = null;
        this.complete(t);
    }

    public RestFuture(final Throwable t)
    {
        this.request = null;
        this.completeExceptionally(t);
    }

    @Override
    public boolean cancel(final boolean mayInterrupt)
    {
        if (this.request != null)
            this.request.cancel();

        return super.cancel(mayInterrupt);
    }

    @Override
    public CompletableFuture<T> toCompletableFuture()
    {
        throw new UnsupportedOperationException("Access to the CompletableFuture is not supported to secure JDA Requester integrity.");
    }
}
