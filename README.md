Perfeito! Aqui está uma versão *completa em Markdown* do seu DSL RSQL/FIQL customizado, pronta para servir como *documentação oficial*, com exemplos de filtros simples e compostos:

---

# 📌 DSL RSQL/FIQL Customizado – Guia Oficial

## 1. Strings (campo com case opcional: :upper / :lower)

| Operador | Significado                  | Exemplo              | SQL gerado / Observação     |
| -------- | ---------------------------- | -------------------- | --------------------------- |
| ==     | Igualdade exata              | name==Danilo       | name = 'Danilo' (literal) |
| ==     | Igualdade exata, campo upper | name:upper==DANILO | UPPER(name) = 'DANILO'    |
| ==     | Igualdade exata, campo lower | name:lower==danilo | LOWER(name) = 'danilo'    |
| ~=     | Contém (like %value%)      | name~=ani          | name LIKE '%ani%'         |
| ~=     | Contém, campo upper          | name:upper~=ANI    | UPPER(name) LIKE '%ANI%'  |
| ~=     | Contém, campo lower          | name:lower~=ani    | LOWER(name) LIKE '%ani%'  |
| ^=     | Começa com                   | name^=Dan          | name LIKE 'Dan%'          |
| ^=     | Começa com, campo upper      | name:upper^=DAN    | UPPER(name) LIKE 'DAN%'   |
| $=     | Termina com                  | name$=ilo          | name LIKE '%ilo'          |
| $=     | Termina com, campo lower     | name:lower$=ilo    | LOWER(name) LIKE '%ilo'   |

> Nota: O valor *não é modificado, a transformação de case é aplicada **apenas no campo*.

---

## 2. Números / Datas

| Operador | Significado        | Exemplo                                    |
| -------- | ------------------ | ------------------------------------------ |
| =gt=   | Maior que          | age=gt=18                                |
| =ge=   | Maior ou igual     | price=ge=100                             |
| =lt=   | Menor que          | birthDate=lt=2025-01-01                  |
| =le=   | Menor ou igual     | createdAt=le=2025-09-03T10:00:00         |
| =btw= | Entre dois valores | price=btw=(10,100)                   |
| =btw= | Datas              | birthDate=btw=(2000-01-01,2010-12-31) |

---

## 3. Listas / Enum

| Operador | Significado       | Exemplo                |
| -------- | ----------------- | ---------------------- |
| =in=   | Está na lista     | role=in=(ADMIN,USER) |
| =out=  | Não está na lista | id=out=(1,2,3)       |

---

## 4. Null / Not null

| Operador  | Significado      | Exemplo             |
| --------- | ---------------- | ------------------- |
| isnull  | Valor é null     | deletedAt=isnull  |
| notnull | Valor não é null | updatedAt=notnull |

---

## 5. Booleanos

| Operador | Significado | Exemplo         |
| -------- | ----------- | --------------- |
| ==     | Igualdade   | active==true  |
| !=     | Diferente   | active!=false |

---

## 6. Combinação lógica

| Símbolo | Significado | Exemplo                  | SQL equivalente               |
| ------- | ----------- | ------------------------ | ----------------------------- |
| ;     | AND         | age=gt=18;active==true | age > 18 AND active = true  |
| ,     | OR          | role==ADMIN,role==USER | role = ADMIN OR role = USER |
| =not= | NOT         | =not=(active==true)    | NOT(active = true)          |

---

## 7. Exemplos de queries completas

*Filtro simples:*


name:upper==DANILO


> Busca nomes iguais a 'DANILO', campo convertido para UPPER().

*Filtro com contains e case lower:*


name:lower~=dan


> Busca nomes que contenham 'dan', usando LOWER(name) LIKE '%dan%'.

*Filtro numérico e booleano:*


age=ge=18;active==true


> Pessoas com idade ≥ 18 *e* ativas.

*Filtro com lista de enums e null check:*


role=in=(ADMIN,USER);deletedAt=isnull


> Usuários com role ADMIN ou USER e que não foram deletados.

*Filtro composto com OR e NOT:*


(age=lt=18,age=gt=65);=not=(active==false)


> Menores de 18 ou maiores de 65, *e* que estão ativos.

---

## 8. Observações finais

* *Campos de string* podem usar :upper ou :lower.
* *Valores não são alterados, a transformação é apenas no **campo*.
* *Datas e números* seguem a sintaxe clássica =gt=, =lt=, =btw=.
* *Listas* usam parênteses e vírgulas: =in=(X,Y,Z).
* *Null e booleanos* têm operadores dedicados (isnull, notnull, ==true, !=false).
* *Combinações lógicas* usam ; (AND), , (OR) e =not=() (NOT).

---

Se você quiser, posso fazer *uma versão visual resumida, tipo **uma tabela compacta com exemplos e operador, pronta para o time de frontend e backend copiar*, tipo um cheat sheet.

Quer que eu faça isso também?