/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.winesms.resource;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Value
@Builder
public class AddWine {

    @NotBlank(message = "Name is required")
    @Size(min = 8, max = 64, message = "Name must be between 8 and 64 characters")
    String name;

    @NotBlank(message = "Type is required")
    @Size(min = 3, max = 64, message = "Type must be between 3 and 64 characters")
    String type;

    @NotBlank(message = "Vintage is required")
    @Size(min = 4, max = 64, message = "Vintage must be between 4 and 64 characters")
    String vintage;

    @NotBlank(message = "Vintage is required")
    @Size(min = 4, max = 64, message = "Vintage must be between 4 and 64 characters")
    String producer;

    @NotBlank(message = "Varietal is required")
    @Size(min = 4, max = 64, message = "Varietal must be between 4 and 64 characters")
    String varietal;

    @Size(min = 4, max = 64, message = "Designation must be between 4 and 64 characters")
    String designation;

    @Size(min = 4, max = 64, message = "Vineyard must be between 4 and 64 characters")
    String vineyard;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 64, message = "Country must be between 2 and 64 characters")
    String country;

    @NotBlank(message = "Region is required")
    @Size(min = 4, max = 64, message = "Region must be between 4 and 64 characters")
    String region;

    @NotBlank(message = "Subregion is required")
    @Size(min = 4, max = 64, message = "Subregion must be between 4 and 64 characters")
    String subregion;

    @NotBlank(message = "Appellation is required")
    @Size(min = 4, max = 64, message = "Appellation must be between 4 and 64 characters")
    String appellation;

}
