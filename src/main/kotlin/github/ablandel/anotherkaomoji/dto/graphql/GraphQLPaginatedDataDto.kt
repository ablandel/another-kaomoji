package github.ablandel.anotherkaomoji.dto.graphql

data class GraphQLPaginatedDataDto<T>(
    val data: List<T>,
    val pagination: GraphQLPaginatedDataPaginationDto,
    val totalCount: Long
)

data class GraphQLPaginatedDataPaginationDto(
    val offset: Int? = null,
    val limit: Int? = null,
    val cursor: Long? = null
)