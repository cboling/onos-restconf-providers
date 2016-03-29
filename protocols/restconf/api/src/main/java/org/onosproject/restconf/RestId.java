/*
 * Copyright 2015 - 2016 Boling Consulting Solutions, bcsw.net
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
package org.onosproject.restconf;

import org.onlab.packet.IpAddress;

import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class represents a device in the SDN network controlled by the RESTCONF protocol
 * <p>
 * This class is immutable
 */
public final class RestId {

    private static final String SCHEME = "restconf";
    private static final IpAddress UNKNOWN = IpAddress.valueOf(0);
    private final IpAddress value;

    /**
     * Default constructor.
     */
    public RestId() {
        this.value = RestId.UNKNOWN;
    }

    /**
     * Constructor from a int value.
     *
     * @param value the value to use.
     */
    public RestId(int value) {
        this.value = IpAddress.valueOf(value);
    }

    /**
     * Constructor from a string.
     *
     * @param value the value to use.
     */
    public RestId(IpAddress value) {
        this.value = value;
    }

    /**
     * Constructor from a string.
     *
     * @param value the value to use.
     */
    public RestId(String value) {
        this.value = IpAddress.valueOf(value);
    }

    /**
     * Get the value of the RestId.
     *
     * @return the value of the RestId.
     */
    public byte[] value() {
        return value.toOctets();
    }

    /**
     * Convert the RestId value to a string.
     *
     * @return the RestId value as a string.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof RestId)) {
            return false;
        }
        RestId otherId = (RestId) other;

        return value == otherId.value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Returns DPID created from the given device URI.
     *
     * @param uri device URI
     *
     * @return dpid
     */
    public static RestId RestId(URI uri) {
        checkArgument(uri.getScheme().equals(SCHEME), "Unsupported URI scheme");
        return new RestId(uri.getSchemeSpecificPart());
    }

    /**
     * Get RESTCONF device ID for this device
     *
     * @param ipAddr
     *
     * @return
     */
    public static RestId valueOf(IpAddress ipAddr) {
        return new RestId(ipAddr);
    }

    /**
     * Produces device URI from the given RestId.
     *
     * @param id device ID
     *
     * @return device URI
     */
    public static URI uri(RestId id) {
        return uri(id.value.toOctets());
    }

    /**
     * Produces device URI from the given RestId octet stream.
     *
     * @param value device ID as octet stream
     *
     * @return device URI
     */
    public static URI uri(byte[] value) {
        try {
            IpAddress ip = (value.length == 4) ? IpAddress.valueOf(IpAddress.Version.INET, value)
                    : IpAddress.valueOf(IpAddress.Version.INET6, value);
            return new URI(SCHEME, ip.toString(), null);
        } catch (URISyntaxException e) {
            return null;
        }
    }
}