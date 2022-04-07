package com.example.demo.controllers;

import com.example.demo.utils.json.Jsonable;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public abstract class BaseController {

    /**
     * Gère une réponse HTTP "404 - Not Found".
     *
     * @param response la réponse HTTP à compléter.
     * @return le payload JSON de la réponse HTTP, contenant le message "Not Found".
     */
    public static String notFound(HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "{ \"message\": \"Not Found\" }";
    }

    /**
     * Gère la réponse HTTP pour retourner une chaine de caractères optionnelle.
     * La réponse HTTP est "200 - OK" si le contenu est présent, et "404 - Nout Found" si le contenu est vide.
     *
     * @param strOpt   la chaine de caractères optionnelle.
     * @param response la réponse HTTP à compléter.
     * @return le payload de la réponse HTTP ou bien un message "Not Found" si le contenu est vide.
     */
    public static String processOptionalString(Optional<String> strOpt, HttpServletResponse response) {
        if (strOpt.isPresent()) {
            response.setStatus(HttpStatus.OK.value());
            return strOpt.get();
        } else {
            return notFound(response);
        }
    }

    /**
     * Gère la réponse HTTP pour retourner un contenu optionnel de type {@link com.example.demo.utils.json.Jsonable}.
     * La réponse HTTP est "200 - OK" si le contenu est présent, et "404 - Nout Found" si le contenu est vide.
     *
     * @param jsonableOpt le contenu optionnel.
     * @param response la réponse HTTP à compléter.
     * @return le payload JSON de la réponse HTTP ou bien un message "Not Found" si le contenu est vide.
     */
    public String processOptionalJsonable(Optional<? extends Jsonable> jsonableOpt, HttpServletResponse response) {
        return processOptionalString(jsonableOpt.map(Jsonable::toJsonString), response);
    }
}
