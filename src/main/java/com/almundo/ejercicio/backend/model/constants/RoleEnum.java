package com.almundo.ejercicio.backend.model.constants;

/**
 * Enum con los roles de empledos del CallCenter
 */
public enum RoleEnum {
    
   SUPERVISOR("SUPERVISOR"), OPERADOR("OPERADOR"), DIRECTOR("DIRECTOR");
   
   private final String role;

   private RoleEnum(String role) {
      this.role = role;
   }

   public String getRole() {
      return role;
   }

   @Override
   public String toString() {
      return role;
   }

    
}
